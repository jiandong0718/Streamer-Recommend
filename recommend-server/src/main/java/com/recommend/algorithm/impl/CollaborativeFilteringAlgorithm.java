package com.recommend.algorithm.impl;

import com.recommend.algorithm.RecommendAlgorithm;
import com.recommend.algorithm.RecommendMetrics;
import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.Game;
import com.recommend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CollaborativeFilteringAlgorithm implements RecommendAlgorithm {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GameMasterService gameMasterService;
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private UserBehaviorService userBehaviorService;
    
    @Autowired
    private OrderService orderService;
    
    // 用户-游戏陪玩评分矩阵
    private Map<Long, Map<Long, Double>> userMasterRatings;
    
    // 用户-游戏评分矩阵
    private Map<Long, Map<Long, Double>> userGameRatings;
    
    // 游戏陪玩相似度矩阵
    private Map<Long, Map<Long, Double>> masterSimilarity;
    
    // 游戏相似度矩阵
    private Map<Long, Map<Long, Double>> gameSimilarity;
    
    @Override
    public List<GameMaster> recommendGameMasters(Long userId, Long gameId, Integer limit) {
        // 1. 获取用户的历史行为数据
        List<Long> userHistory = getUserHistory(userId);
        
        // 2. 计算用户与游戏陪玩的相似度
        Map<Long, Double> masterScores = new HashMap<>();
        for (Long masterId : getAllMasters()) {
            if (gameId != null && !isMasterGameMatch(masterId, gameId)) {
                continue;
            }
            double score = calculateUserMasterSimilarity(userId, masterId);
            masterScores.put(masterId, score);
        }
        
        // 3. 排序并返回推荐结果
        return masterScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> gameMasterService.getGameMasterById(entry.getKey()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Game> recommendGames(Long userId, Integer limit) {
        // 1. 获取用户的历史行为数据
        List<Long> userHistory = getUserHistory(userId);
        
        // 2. 计算用户与游戏的相似度
        Map<Long, Double> gameScores = new HashMap<>();
        for (Long gameId : getAllGames()) {
            double score = calculateUserGameSimilarity(userId, gameId);
            gameScores.put(gameId, score);
        }
        
        // 3. 排序并返回推荐结果
        return gameScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> gameService.getGameById(entry.getKey()))
                .collect(Collectors.toList());
    }
    
    @Override
    public double calculateUserMasterSimilarity(Long userId, Long masterId) {
        // 1. 获取用户的历史行为数据
        List<Long> userHistory = getUserHistory(userId);
        
        // 2. 计算用户与游戏陪玩的相似度
        double similarity = 0.0;
        for (Long historyMasterId : userHistory) {
            similarity += calculateMasterSimilarity(historyMasterId, masterId);
        }
        
        return similarity / userHistory.size();
    }
    
    @Override
    public double calculateUserGameSimilarity(Long userId, Long gameId) {
        // 1. 获取用户的历史行为数据
        List<Long> userHistory = getUserHistory(userId);
        
        // 2. 计算用户与游戏的相似度
        double similarity = 0.0;
        for (Long historyGameId : userHistory) {
            similarity += calculateGameSimilarity(historyGameId, gameId);
        }
        
        return similarity / userHistory.size();
    }
    
    @Override
    public double calculateMasterSimilarity(Long masterId1, Long masterId2) {
        if (masterSimilarity.containsKey(masterId1) && 
            masterSimilarity.get(masterId1).containsKey(masterId2)) {
            return masterSimilarity.get(masterId1).get(masterId2);
        }
        
        // 1. 获取两个游戏陪玩的特征向量
        List<Double> features1 = getMasterFeatures(masterId1);
        List<Double> features2 = getMasterFeatures(masterId2);
        
        // 2. 计算余弦相似度
        double similarity = calculateCosineSimilarity(features1, features2);
        
        // 3. 缓存相似度结果
        masterSimilarity.computeIfAbsent(masterId1, k -> new HashMap<>())
                .put(masterId2, similarity);
        masterSimilarity.computeIfAbsent(masterId2, k -> new HashMap<>())
                .put(masterId1, similarity);
        
        return similarity;
    }
    
    @Override
    public double calculateGameSimilarity(Long gameId1, Long gameId2) {
        if (gameSimilarity.containsKey(gameId1) && 
            gameSimilarity.get(gameId1).containsKey(gameId2)) {
            return gameSimilarity.get(gameId1).get(gameId2);
        }
        
        // 1. 获取两个游戏的特征向量
        List<Double> features1 = getGameFeatures(gameId1);
        List<Double> features2 = getGameFeatures(gameId2);
        
        // 2. 计算余弦相似度
        double similarity = calculateCosineSimilarity(features1, features2);
        
        // 3. 缓存相似度结果
        gameSimilarity.computeIfAbsent(gameId1, k -> new HashMap<>())
                .put(gameId2, similarity);
        gameSimilarity.computeIfAbsent(gameId2, k -> new HashMap<>())
                .put(gameId1, similarity);
        
        return similarity;
    }
    
    @Override
    public void updateUserFeatures(Long userId) {
        // 1. 获取用户的行为数据
        List<Long> userHistory = getUserHistory(userId);
        
        // 2. 更新用户-游戏陪玩评分矩阵
        Map<Long, Double> masterRatings = new HashMap<>();
        for (Long masterId : userHistory) {
            double rating = calculateUserMasterRating(userId, masterId);
            masterRatings.put(masterId, rating);
        }
        userMasterRatings.put(userId, masterRatings);
        
        // 3. 更新用户-游戏评分矩阵
        Map<Long, Double> gameRatings = new HashMap<>();
        for (Long gameId : userHistory) {
            double rating = calculateUserGameRating(userId, gameId);
            gameRatings.put(gameId, rating);
        }
        userGameRatings.put(userId, gameRatings);
    }
    
    @Override
    public void updateMasterFeatures(Long masterId) {
        // 1. 获取游戏陪玩的行为数据
        List<Long> masterHistory = getMasterHistory(masterId);
        
        // 2. 更新游戏陪玩相似度矩阵
        for (Long otherMasterId : getAllMasters()) {
            if (!otherMasterId.equals(masterId)) {
                double similarity = calculateMasterSimilarity(masterId, otherMasterId);
                masterSimilarity.computeIfAbsent(masterId, k -> new HashMap<>())
                        .put(otherMasterId, similarity);
            }
        }
    }
    
    @Override
    public void updateGameFeatures(Long gameId) {
        // 1. 获取游戏的行为数据
        List<Long> gameHistory = getGameHistory(gameId);
        
        // 2. 更新游戏相似度矩阵
        for (Long otherGameId : getAllGames()) {
            if (!otherGameId.equals(gameId)) {
                double similarity = calculateGameSimilarity(gameId, otherGameId);
                gameSimilarity.computeIfAbsent(gameId, k -> new HashMap<>())
                        .put(otherGameId, similarity);
            }
        }
    }
    
    @Override
    public void trainModel() {
        // 1. 初始化数据结构
        userMasterRatings = new HashMap<>();
        userGameRatings = new HashMap<>();
        masterSimilarity = new HashMap<>();
        gameSimilarity = new HashMap<>();
        
        // 2. 更新所有用户的特征向量
        for (Long userId : getAllUsers()) {
            updateUserFeatures(userId);
        }
        
        // 3. 更新所有游戏陪玩的特征向量
        for (Long masterId : getAllMasters()) {
            updateMasterFeatures(masterId);
        }
        
        // 4. 更新所有游戏的特征向量
        for (Long gameId : getAllGames()) {
            updateGameFeatures(gameId);
        }
    }
    
    @Override
    public RecommendMetrics evaluateModel() {
        // 1. 准备评估数据
        List<Long> testUsers = getTestUsers();
        List<Long> testMasters = getTestMasters();
        List<Long> testGames = getTestGames();
        
        // 2. 计算评估指标
        double precision = calculatePrecision(testUsers, testMasters);
        double recall = calculateRecall(testUsers, testMasters);
        double mrr = calculateMRR(testUsers, testMasters);
        double ndcg = calculateNDCG(testUsers, testMasters);
        double coverage = calculateCoverage(testUsers, testMasters);
        double diversity = calculateDiversity(testUsers, testMasters);
        double novelty = calculateNovelty(testUsers, testMasters);
        double timeliness = calculateTimeliness(testUsers, testMasters);
        double coldStartPerformance = calculateColdStartPerformance(testUsers, testMasters);
        
        // 3. 构建评估结果
        RecommendMetrics metrics = RecommendMetrics.builder()
                .precision(precision)
                .recall(recall)
                .mrr(mrr)
                .ndcg(ndcg)
                .coverage(coverage)
                .diversity(diversity)
                .novelty(novelty)
                .timeliness(timeliness)
                .coldStartPerformance(coldStartPerformance)
                .build();
        
        // 4. 计算F1分数
        metrics.calculateF1Score();
        
        return metrics;
    }
    
    // 辅助方法
    
    private List<Long> getUserHistory(Long userId) {
        return userBehaviorService.getUserBehaviorsByUserId(userId).stream()
                .map(behavior -> behavior.getTargetId())
                .collect(Collectors.toList());
    }
    
    private List<Long> getMasterHistory(Long masterId) {
        return orderService.getOrdersByMasterId(masterId).stream()
                .map(order -> order.getUserId())
                .collect(Collectors.toList());
    }
    
    private List<Long> getGameHistory(Long gameId) {
        return orderService.getOrdersByGameId(gameId).stream()
                .map(order -> order.getUserId())
                .collect(Collectors.toList());
    }
    
    private List<Long> getAllUsers() {
        return userService.getUserList().stream()
                .map(user -> user.getId())
                .collect(Collectors.toList());
    }
    
    private List<Long> getAllMasters() {
        return gameMasterService.getGameMasterList().stream()
                .map(master -> master.getId())
                .collect(Collectors.toList());
    }
    
    private List<Long> getAllGames() {
        return gameService.getGameList().stream()
                .map(game -> game.getId())
                .collect(Collectors.toList());
    }
    
    private boolean isMasterGameMatch(Long masterId, Long gameId) {
        return gameMasterService.getGameMasterGames(masterId).contains(gameId);
    }
    
    private List<Double> getMasterFeatures(Long masterId) {
        // 实现游戏陪玩特征提取逻辑
        return new ArrayList<>();
    }
    
    private List<Double> getGameFeatures(Long gameId) {
        // 实现游戏特征提取逻辑
        return new ArrayList<>();
    }
    
    private double calculateCosineSimilarity(List<Double> features1, List<Double> features2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (int i = 0; i < features1.size(); i++) {
            dotProduct += features1.get(i) * features2.get(i);
            norm1 += features1.get(i) * features1.get(i);
            norm2 += features2.get(i) * features2.get(i);
        }
        
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
    
    private double calculateUserMasterRating(Long userId, Long masterId) {
        // 实现用户对游戏陪玩的评分计算逻辑
        return 0.0;
    }
    
    private double calculateUserGameRating(Long userId, Long gameId) {
        // 实现用户对游戏的评分计算逻辑
        return 0.0;
    }
    
    private List<Long> getTestUsers() {
        // 实现测试用户选择逻辑
        return new ArrayList<>();
    }
    
    private List<Long> getTestMasters() {
        // 实现测试游戏陪玩选择逻辑
        return new ArrayList<>();
    }
    
    private List<Long> getTestGames() {
        // 实现测试游戏选择逻辑
        return new ArrayList<>();
    }
    
    private double calculatePrecision(List<Long> testUsers, List<Long> testMasters) {
        // 实现准确率计算逻辑
        return 0.0;
    }
    
    private double calculateRecall(List<Long> testUsers, List<Long> testMasters) {
        // 实现召回率计算逻辑
        return 0.0;
    }
    
    private double calculateMRR(List<Long> testUsers, List<Long> testMasters) {
        // 实现MRR计算逻辑
        return 0.0;
    }
    
    private double calculateNDCG(List<Long> testUsers, List<Long> testMasters) {
        // 实现NDCG计算逻辑
        return 0.0;
    }
    
    private double calculateCoverage(List<Long> testUsers, List<Long> testMasters) {
        // 实现覆盖率计算逻辑
        return 0.0;
    }
    
    private double calculateDiversity(List<Long> testUsers, List<Long> testMasters) {
        // 实现多样性计算逻辑
        return 0.0;
    }
    
    private double calculateNovelty(List<Long> testUsers, List<Long> testMasters) {
        // 实现新颖性计算逻辑
        return 0.0;
    }
    
    private double calculateTimeliness(List<Long> testUsers, List<Long> testMasters) {
        // 实现实时性计算逻辑
        return 0.0;
    }
    
    private double calculateColdStartPerformance(List<Long> testUsers, List<Long> testMasters) {
        // 实现冷启动性能计算逻辑
        return 0.0;
    }
} 