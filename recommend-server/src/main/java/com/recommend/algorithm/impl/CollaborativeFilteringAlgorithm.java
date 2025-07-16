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
    
    @Autowired
    private UserProfileService userProfileService;
    
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
        try {
            GameMaster master = gameMasterService.getGameMasterById(masterId);
            if (master == null) {
                return new ArrayList<>();
            }
            
            List<Double> features = new ArrayList<>();
            
            // 1. 评分特征
            features.add(master.getScore() != null ? master.getScore().doubleValue() : 0.0);
            
            // 2. 订单数特征（归一化）
            features.add(master.getOrderCount() != null ? Math.min(1.0, master.getOrderCount() / 100.0) : 0.0);
            
            // 3. 价格特征（归一化）
            features.add(master.getPrice() != null ? Math.min(1.0, master.getPrice().doubleValue() / 500.0) : 0.0);
            
            // 4. 在线状态特征
            features.add(master.getStatus() != null && master.getStatus() == 1 ? 1.0 : 0.0);
            
            // 5. 游戏类型数量特征
            int gameTypeCount = master.getGameTypes() != null ? master.getGameTypes().split(",").length : 0;
            features.add(Math.min(1.0, gameTypeCount / 10.0));
            
            // 6. 标签数量特征
            int tagCount = master.getTags() != null ? master.getTags().split(",").length : 0;
            features.add(Math.min(1.0, tagCount / 20.0));
            
            // 7. 注册时长特征
            if (master.getCreateTime() != null) {
                long daysSinceCreation = (System.currentTimeMillis() - master.getCreateTime().getTime()) / (1000 * 60 * 60 * 24);
                features.add(Math.min(1.0, daysSinceCreation / 365.0)); // 按年归一化
            } else {
                features.add(0.0);
            }
            
            // 8. 活跃度特征
            if (master.getUpdateTime() != null) {
                long daysSinceUpdate = (System.currentTimeMillis() - master.getUpdateTime().getTime()) / (1000 * 60 * 60 * 24);
                features.add(Math.max(0.0, 1.0 - daysSinceUpdate / 30.0)); // 30天内活跃度较高
            } else {
                features.add(0.0);
            }
            
            return features;
        } catch (Exception e) {
            log.error("提取陪玩师特征失败，陪玩师ID: {}", masterId, e);
            return new ArrayList<>();
        }
    }
    
    private List<Double> getGameFeatures(Long gameId) {
        try {
            Game game = gameService.getGameById(gameId);
            if (game == null) {
                return new ArrayList<>();
            }
            
            List<Double> features = new ArrayList<>();
            
            // 1. 游戏热度特征（基于ID，实际应该有专门的热度字段）
            features.add(Math.min(1.0, game.getId() / 1000.0));
            
            // 2. 游戏状态特征
            features.add(game.getStatus() != null && game.getStatus() == 1 ? 1.0 : 0.0);
            
            // 3. 游戏类型编码（简化处理）
            if (game.getType() != null) {
                // 将游戏类型转换为数值特征
                features.add(game.getType().hashCode() % 100 / 100.0);
            } else {
                features.add(0.0);
            }
            
            // 4. 标签数量特征
            int tagCount = game.getTags() != null ? game.getTags().split(",").length : 0;
            features.add(Math.min(1.0, tagCount / 10.0));
            
            // 5. 创建时长特征
            if (game.getCreateTime() != null) {
                long daysSinceCreation = (System.currentTimeMillis() - game.getCreateTime().getTime()) / (1000 * 60 * 60 * 24);
                features.add(Math.min(1.0, daysSinceCreation / 365.0));
            } else {
                features.add(0.0);
            }
            
            // 6. 更新活跃度特征
            if (game.getUpdateTime() != null) {
                long daysSinceUpdate = (System.currentTimeMillis() - game.getUpdateTime().getTime()) / (1000 * 60 * 60 * 24);
                features.add(Math.max(0.0, 1.0 - daysSinceUpdate / 90.0)); // 90天内更新活跃度较高
            } else {
                features.add(0.0);
            }
            
            return features;
        } catch (Exception e) {
            log.error("提取游戏特征失败，游戏ID: {}", gameId, e);
            return new ArrayList<>();
        }
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
        try {
            // 基于用户历史行为计算对陪玩师的隐式评分
            UserProfile userProfile = userProfileService.getUserProfile(userId);
            GameMaster master = gameMasterService.getGameMasterById(masterId);
            
            if (userProfile == null || master == null) {
                return 0.0;
            }
            
            double rating = 0.0;
            
            // 1. 基于标签匹配度
            if (userProfile.getTags() != null && master.getTags() != null) {
                List<String> userTags = Arrays.asList(userProfile.getTags().split(","));
                List<String> masterTags = Arrays.asList(master.getTags().split(","));
                long matchCount = userTags.stream().filter(masterTags::contains).count();
                rating += (double) matchCount / userTags.size() * 2.0; // 0-2分
            }
            
            // 2. 基于游戏类型匹配度
            if (userProfile.getGameTypes() != null && master.getGameTypes() != null) {
                List<String> userGameTypes = Arrays.asList(userProfile.getGameTypes().split(","));
                List<String> masterGameTypes = Arrays.asList(master.getGameTypes().split(","));
                long gameMatchCount = userGameTypes.stream().filter(masterGameTypes::contains).count();
                rating += (double) gameMatchCount / userGameTypes.size() * 1.5; // 0-1.5分
            }
            
            // 3. 基于地区匹配
            if (userProfile.getRegion() != null && master.getRegion() != null) {
                if (userProfile.getRegion().equals(master.getRegion())) {
                    rating += 1.0;
                }
            }
            
            // 4. 基于陪玩师质量
            if (master.getScore() != null) {
                rating += master.getScore().doubleValue() * 0.3; // 最高1.5分
            }
            
            return Math.min(5.0, rating); // 限制在5分以内
        } catch (Exception e) {
            log.error("计算用户-陪玩师评分失败，用户ID: {}, 陪玩师ID: {}", userId, masterId, e);
            return 0.0;
        }
    }
    
    private double calculateUserGameRating(Long userId, Long gameId) {
        try {
            // 基于用户历史行为计算对游戏的隐式评分
            UserProfile userProfile = userProfileService.getUserProfile(userId);
            Game game = gameService.getGameById(gameId);
            
            if (userProfile == null || game == null) {
                return 0.0;
            }
            
            double rating = 0.0;
            
            // 1. 基于游戏类型偏好
            if (userProfile.getGameTypes() != null && game.getType() != null) {
                List<String> userGameTypes = Arrays.asList(userProfile.getGameTypes().split(","));
                if (userGameTypes.contains(game.getType())) {
                    rating += 3.0; // 喜欢的游戏类型给高分
                } else {
                    rating += 1.0; // 其他类型给低分
                }
            }
            
            // 2. 基于游戏标签匹配
            if (userProfile.getTags() != null && game.getTags() != null) {
                List<String> userTags = Arrays.asList(userProfile.getTags().split(","));
                List<String> gameTags = Arrays.asList(game.getTags().split(","));
                long matchCount = userTags.stream().filter(gameTags::contains).count();
                rating += (double) matchCount / userTags.size() * 1.5;
            }
            
            // 3. 基于游戏热度（简化处理）
            rating += Math.min(0.5, game.getId() / 1000.0);
            
            return Math.min(5.0, rating);
        } catch (Exception e) {
            log.error("计算用户-游戏评分失败，用户ID: {}, 游戏ID: {}", userId, gameId, e);
            return 0.0;
        }
    }
    
    private List<Long> getTestUsers() {
        try {
            // 获取有足够历史行为数据的用户作为测试用户
            List<Long> allUsers = getAllUsers();
            return allUsers.stream()
                .filter(userId -> {
                    // 至少有5个历史行为的用户才能作为测试用户
                    List<Long> userHistory = getUserHistory(userId);
                    return userHistory.size() >= 5;
                })
                .limit(100) // 限制测试用户数量，避免计算过慢
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取测试用户失败", e);
            return new ArrayList<>();
        }
    }
    
    private List<Long> getTestMasters() {
        try {
            // 获取有足够订单数据的陪玩师作为测试对象
            List<Long> allMasters = getAllMasters();
            return allMasters.stream()
                .filter(masterId -> {
                    GameMaster master = gameMasterService.getGameMasterById(masterId);
                    // 至少有10个订单的陪玩师才能作为测试对象
                    return master != null && master.getOrderCount() != null && master.getOrderCount() >= 10;
                })
                .limit(200) // 限制测试陪玩师数量
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取测试陪玩师失败", e);
            return new ArrayList<>();
        }
    }
    
    private List<Long> getTestGames() {
        try {
            // 获取活跃的游戏作为测试对象
            List<Long> allGames = getAllGames();
            return allGames.stream()
                .filter(gameId -> {
                    Game game = gameService.getGameById(gameId);
                    // 只选择启用状态的游戏
                    return game != null && game.getStatus() != null && game.getStatus() == 1;
                })
                .limit(50) // 限制测试游戏数量
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取测试游戏失败", e);
            return new ArrayList<>();
        }
    }
    
    private double calculatePrecision(List<Long> testUsers, List<Long> testMasters) {
        try {
            double totalPrecision = 0.0;
            int validUsers = 0;
            
            for (Long userId : testUsers) {
                // 获取推荐结果
                List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
                if (recommendations.isEmpty()) continue;
                
                // 获取用户真实喜欢的陪玩师（基于历史高评分订单）
                Set<Long> actualLikes = getUserActualLikes(userId);
                if (actualLikes.isEmpty()) continue;
                
                // 计算推荐结果中的命中数
                long hits = recommendations.stream()
                    .mapToLong(master -> master.getId())
                    .filter(actualLikes::contains)
                    .count();
                
                double precision = (double) hits / recommendations.size();
                totalPrecision += precision;
                validUsers++;
            }
            
            return validUsers > 0 ? totalPrecision / validUsers : 0.0;
        } catch (Exception e) {
            log.error("计算准确率失败", e);
            return 0.0;
        }
    }
    
    private double calculateRecall(List<Long> testUsers, List<Long> testMasters) {
        try {
            double totalRecall = 0.0;
            int validUsers = 0;
            
            for (Long userId : testUsers) {
                // 获取推荐结果
                List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
                if (recommendations.isEmpty()) continue;
                
                // 获取用户真实喜欢的陪玩师
                Set<Long> actualLikes = getUserActualLikes(userId);
                if (actualLikes.isEmpty()) continue;
                
                // 计算推荐结果中的命中数
                long hits = recommendations.stream()
                    .mapToLong(master -> master.getId())
                    .filter(actualLikes::contains)
                    .count();
                
                double recall = (double) hits / actualLikes.size();
                totalRecall += recall;
                validUsers++;
            }
            
            return validUsers > 0 ? totalRecall / validUsers : 0.0;
        } catch (Exception e) {
            log.error("计算召回率失败", e);
            return 0.0;
        }
    }
    
    private double calculateMRR(List<Long> testUsers, List<Long> testMasters) {
        try {
            double totalMRR = 0.0;
            int validUsers = 0;
            
            for (Long userId : testUsers) {
                // 获取推荐结果
                List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
                if (recommendations.isEmpty()) continue;
                
                // 获取用户真实喜欢的陪玩师
                Set<Long> actualLikes = getUserActualLikes(userId);
                if (actualLikes.isEmpty()) continue;
                
                // 找到第一个命中的位置
                int firstHitRank = -1;
                for (int i = 0; i < recommendations.size(); i++) {
                    if (actualLikes.contains(recommendations.get(i).getId())) {
                        firstHitRank = i + 1; // 排名从1开始
                        break;
                    }
                }
                
                if (firstHitRank > 0) {
                    totalMRR += 1.0 / firstHitRank;
                }
                validUsers++;
            }
            
            return validUsers > 0 ? totalMRR / validUsers : 0.0;
        } catch (Exception e) {
            log.error("计算MRR失败", e);
            return 0.0;
        }
    }
    
    private double calculateNDCG(List<Long> testUsers, List<Long> testMasters) {
        try {
            double totalNDCG = 0.0;
            int validUsers = 0;
            
            for (Long userId : testUsers) {
                // 获取推荐结果
                List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
                if (recommendations.isEmpty()) continue;
                
                // 获取用户对陪玩师的真实评分
                Map<Long, Double> actualRatings = getUserActualRatings(userId);
                if (actualRatings.isEmpty()) continue;
                
                // 计算DCG
                double dcg = 0.0;
                for (int i = 0; i < recommendations.size(); i++) {
                    Long masterId = recommendations.get(i).getId();
                    double rating = actualRatings.getOrDefault(masterId, 0.0);
                    dcg += (Math.pow(2, rating) - 1) / (Math.log(i + 2) / Math.log(2));
                }
                
                // 计算IDCG（理想DCG）
                List<Double> sortedRatings = actualRatings.values().stream()
                    .sorted(Collections.reverseOrder())
                    .limit(recommendations.size())
                    .collect(Collectors.toList());
                
                double idcg = 0.0;
                for (int i = 0; i < sortedRatings.size(); i++) {
                    double rating = sortedRatings.get(i);
                    idcg += (Math.pow(2, rating) - 1) / (Math.log(i + 2) / Math.log(2));
                }
                
                if (idcg > 0) {
                    totalNDCG += dcg / idcg;
                }
                validUsers++;
            }
            
            return validUsers > 0 ? totalNDCG / validUsers : 0.0;
        } catch (Exception e) {
            log.error("计算NDCG失败", e);
            return 0.0;
        }
    }
    
    private double calculateCoverage(List<Long> testUsers, List<Long> testMasters) {
        try {
            // 计算推荐覆盖率：被推荐的陪玩师数量 / 总陪玩师数量
            Set<Long> recommendedMasters = new HashSet<>();
            
            for (Long userId : testUsers) {
                List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
                recommendations.stream()
                    .map(GameMaster::getId)
                    .forEach(recommendedMasters::add);
            }
            
            int totalMasters = getAllMasters().size();
            return totalMasters > 0 ? (double) recommendedMasters.size() / totalMasters : 0.0;
        } catch (Exception e) {
            log.error("计算覆盖率失败", e);
            return 0.0;
        }
    }
    
    private double calculateDiversity(List<Long> testUsers, List<Long> testMasters) {
        try {
            double totalDiversity = 0.0;
            int validUsers = 0;
            
            for (Long userId : testUsers) {
                List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
                if (recommendations.size() < 2) continue;
                
                // 计算推荐列表内的多样性（基于游戏类型）
                Set<String> gameTypes = new HashSet<>();
                for (GameMaster master : recommendations) {
                    if (master.getGameTypes() != null) {
                        String[] types = master.getGameTypes().split(",");
                        gameTypes.addAll(Arrays.asList(types));
                    }
                }
                
                // 多样性 = 不同游戏类型数量 / 推荐数量
                double diversity = (double) gameTypes.size() / recommendations.size();
                totalDiversity += diversity;
                validUsers++;
            }
            
            return validUsers > 0 ? totalDiversity / validUsers : 0.0;
        } catch (Exception e) {
            log.error("计算多样性失败", e);
            return 0.0;
        }
    }
    
    private double calculateNovelty(List<Long> testUsers, List<Long> testMasters) {
        try {
            double totalNovelty = 0.0;
            int validUsers = 0;
            
            for (Long userId : testUsers) {
                List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
                if (recommendations.isEmpty()) continue;
                
                // 计算新颖性：新陪玩师（注册时间较短）的比例
                long novelMasters = recommendations.stream()
                    .filter(master -> isNovelMaster(master.getId()))
                    .count();
                
                double novelty = (double) novelMasters / recommendations.size();
                totalNovelty += novelty;
                validUsers++;
            }
            
            return validUsers > 0 ? totalNovelty / validUsers : 0.0;
        } catch (Exception e) {
            log.error("计算新颖性失败", e);
            return 0.0;
        }
    }
    
    private double calculateTimeliness(List<Long> testUsers, List<Long> testMasters) {
        try {
            double totalTimeliness = 0.0;
            int validUsers = 0;
            
            for (Long userId : testUsers) {
                List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
                if (recommendations.isEmpty()) continue;
                
                // 计算时效性：最近活跃的陪玩师比例
                long activeRecentlyCount = recommendations.stream()
                    .filter(master -> isRecentlyActive(master))
                    .count();
                
                double timeliness = (double) activeRecentlyCount / recommendations.size();
                totalTimeliness += timeliness;
                validUsers++;
            }
            
            return validUsers > 0 ? totalTimeliness / validUsers : 0.0;
        } catch (Exception e) {
            log.error("计算时效性失败", e);
            return 0.0;
        }
    }
    
    /**
     * 判断是否为新颖的陪玩师（注册时间较短）
     */
    private boolean isNovelMaster(Long masterId) {
        try {
            GameMaster master = gameMasterService.getGameMasterById(masterId);
            if (master == null || master.getCreateTime() == null) {
                return false;
            }
            
            // 注册60天内的陪玩师认为是新颖的
            long daysSinceRegistration = (System.currentTimeMillis() - master.getCreateTime().getTime()) / (1000 * 60 * 60 * 24);
            return daysSinceRegistration <= 60;
        } catch (Exception e) {
            log.error("判断新颖陪玩师失败，陪玩师ID: {}", masterId, e);
            return false;
        }
    }
    
    /**
     * 判断陪玩师是否最近活跃
     */
    private boolean isRecentlyActive(GameMaster master) {
        if (master.getUpdateTime() == null) {
            return false;
        }
        
        // 7天内有更新认为是最近活跃
        long daysSinceUpdate = (System.currentTimeMillis() - master.getUpdateTime().getTime()) / (1000 * 60 * 60 * 24);
        return daysSinceUpdate <= 7;
    }
    
    private double calculateColdStartPerformance(List<Long> testUsers, List<Long> testMasters) {
        try {
            // 获取新用户（注册时间较短的用户）
            List<Long> newUsers = testUsers.stream()
                .filter(this::isNewUser)
                .collect(Collectors.toList());
            
            if (newUsers.isEmpty()) {
                return 0.0;
            }
            
            double totalPerformance = 0.0;
            int validUsers = 0;
            
            for (Long userId : newUsers) {
                // 为新用户生成推荐
                List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
                if (recommendations.isEmpty()) continue;
                
                // 计算推荐质量（基于陪玩师的平均评分）
                double avgScore = recommendations.stream()
                    .filter(master -> master.getScore() != null)
                    .mapToDouble(master -> master.getScore().doubleValue())
                    .average()
                    .orElse(0.0);
                
                totalPerformance += avgScore / 5.0; // 归一化到0-1
                validUsers++;
            }
            
            return validUsers > 0 ? totalPerformance / validUsers : 0.0;
        } catch (Exception e) {
            log.error("计算冷启动性能失败", e);
            return 0.0;
        }
    }
    
    /**
     * 获取用户真实喜欢的陪玩师列表
     */
    private Set<Long> getUserActualLikes(Long userId) {
        try {
            // 基于用户的历史高评分订单来判断喜好
            return orderService.getOrdersByUserId(userId).stream()
                .filter(order -> order.getRating() != null && order.getRating() >= 4.0)
                .map(order -> order.getMasterId())
                .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("获取用户真实喜好失败，用户ID: {}", userId, e);
            return new HashSet<>();
        }
    }
    
    /**
     * 获取用户对陪玩师的真实评分
     */
    private Map<Long, Double> getUserActualRatings(Long userId) {
        try {
            Map<Long, Double> ratings = new HashMap<>();
            orderService.getOrdersByUserId(userId).stream()
                .filter(order -> order.getRating() != null)
                .forEach(order -> ratings.put(order.getMasterId(), order.getRating()));
            return ratings;
        } catch (Exception e) {
            log.error("获取用户真实评分失败，用户ID: {}", userId, e);
            return new HashMap<>();
        }
    }
    
    /**
     * 判断是否为新用户
     */
    private boolean isNewUser(Long userId) {
        try {
            UserProfile userProfile = userProfileService.getUserProfile(userId);
            if (userProfile == null || userProfile.getCreateTime() == null) {
                return false;
            }
            
            // 注册30天内的用户认为是新用户
            long daysSinceRegistration = (System.currentTimeMillis() - userProfile.getCreateTime().getTime()) / (1000 * 60 * 60 * 24);
            return daysSinceRegistration <= 30;
        } catch (Exception e) {
            log.error("判断新用户失败，用户ID: {}", userId, e);
            return false;
        }
    }
} 