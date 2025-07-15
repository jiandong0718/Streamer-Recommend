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
public class HybridAlgorithm implements RecommendAlgorithm {
    
    @Autowired
    private CollaborativeFilteringAlgorithm collaborativeFilteringAlgorithm;
    
    @Autowired
    private ContentBasedAlgorithm contentBasedAlgorithm;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GameMasterService gameMasterService;
    
    @Autowired
    private GameService gameService;
    
    // 协同过滤算法的权重
    private double collaborativeWeight = 0.6;
    
    // 基于内容的算法的权重
    private double contentBasedWeight = 0.4;
    
    @Override
    public List<GameMaster> recommendGameMasters(Long userId, Long gameId, Integer limit) {
        // 1. 获取协同过滤算法的推荐结果
        List<GameMaster> collaborativeResults = collaborativeFilteringAlgorithm.recommendGameMasters(userId, gameId, limit);
        
        // 2. 获取基于内容的算法的推荐结果
        List<GameMaster> contentBasedResults = contentBasedAlgorithm.recommendGameMasters(userId, gameId, limit);
        
        // 3. 合并推荐结果
        Map<Long, Double> masterScores = new HashMap<>();
        
        // 3.1 处理协同过滤的结果
        for (int i = 0; i < collaborativeResults.size(); i++) {
            GameMaster master = collaborativeResults.get(i);
            double score = collaborativeWeight * (1.0 / (i + 1));
            masterScores.merge(master.getId(), score, Double::sum);
        }
        
        // 3.2 处理基于内容的结果
        for (int i = 0; i < contentBasedResults.size(); i++) {
            GameMaster master = contentBasedResults.get(i);
            double score = contentBasedWeight * (1.0 / (i + 1));
            masterScores.merge(master.getId(), score, Double::sum);
        }
        
        // 4. 排序并返回最终结果
        return masterScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> gameMasterService.getGameMasterById(entry.getKey()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Game> recommendGames(Long userId, Integer limit) {
        // 1. 获取协同过滤算法的推荐结果
        List<Game> collaborativeResults = collaborativeFilteringAlgorithm.recommendGames(userId, limit);
        
        // 2. 获取基于内容的算法的推荐结果
        List<Game> contentBasedResults = contentBasedAlgorithm.recommendGames(userId, limit);
        
        // 3. 合并推荐结果
        Map<Long, Double> gameScores = new HashMap<>();
        
        // 3.1 处理协同过滤的结果
        for (int i = 0; i < collaborativeResults.size(); i++) {
            Game game = collaborativeResults.get(i);
            double score = collaborativeWeight * (1.0 / (i + 1));
            gameScores.merge(game.getId(), score, Double::sum);
        }
        
        // 3.2 处理基于内容的结果
        for (int i = 0; i < contentBasedResults.size(); i++) {
            Game game = contentBasedResults.get(i);
            double score = contentBasedWeight * (1.0 / (i + 1));
            gameScores.merge(game.getId(), score, Double::sum);
        }
        
        // 4. 排序并返回最终结果
        return gameScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> gameService.getGameById(entry.getKey()))
                .collect(Collectors.toList());
    }
    
    @Override
    public double calculateUserMasterSimilarity(Long userId, Long masterId) {
        // 1. 获取两个算法的相似度
        double collaborativeSimilarity = collaborativeFilteringAlgorithm.calculateUserMasterSimilarity(userId, masterId);
        double contentBasedSimilarity = contentBasedAlgorithm.calculateUserMasterSimilarity(userId, masterId);
        
        // 2. 加权合并相似度
        return collaborativeWeight * collaborativeSimilarity + contentBasedWeight * contentBasedSimilarity;
    }
    
    @Override
    public double calculateUserGameSimilarity(Long userId, Long gameId) {
        // 1. 获取两个算法的相似度
        double collaborativeSimilarity = collaborativeFilteringAlgorithm.calculateUserGameSimilarity(userId, gameId);
        double contentBasedSimilarity = contentBasedAlgorithm.calculateUserGameSimilarity(userId, gameId);
        
        // 2. 加权合并相似度
        return collaborativeWeight * collaborativeSimilarity + contentBasedWeight * contentBasedSimilarity;
    }
    
    @Override
    public double calculateMasterSimilarity(Long masterId1, Long masterId2) {
        // 1. 获取两个算法的相似度
        double collaborativeSimilarity = collaborativeFilteringAlgorithm.calculateMasterSimilarity(masterId1, masterId2);
        double contentBasedSimilarity = contentBasedAlgorithm.calculateMasterSimilarity(masterId1, masterId2);
        
        // 2. 加权合并相似度
        return collaborativeWeight * collaborativeSimilarity + contentBasedWeight * contentBasedSimilarity;
    }
    
    @Override
    public double calculateGameSimilarity(Long gameId1, Long gameId2) {
        // 1. 获取两个算法的相似度
        double collaborativeSimilarity = collaborativeFilteringAlgorithm.calculateGameSimilarity(gameId1, gameId2);
        double contentBasedSimilarity = contentBasedAlgorithm.calculateGameSimilarity(gameId1, gameId2);
        
        // 2. 加权合并相似度
        return collaborativeWeight * collaborativeSimilarity + contentBasedWeight * contentBasedSimilarity;
    }
    
    @Override
    public void updateUserFeatures(Long userId) {
        // 更新两个算法的用户特征
        collaborativeFilteringAlgorithm.updateUserFeatures(userId);
        contentBasedAlgorithm.updateUserFeatures(userId);
    }
    
    @Override
    public void updateMasterFeatures(Long masterId) {
        // 更新两个算法的游戏陪玩特征
        collaborativeFilteringAlgorithm.updateMasterFeatures(masterId);
        contentBasedAlgorithm.updateMasterFeatures(masterId);
    }
    
    @Override
    public void updateGameFeatures(Long gameId) {
        // 更新两个算法的游戏特征
        collaborativeFilteringAlgorithm.updateGameFeatures(gameId);
        contentBasedAlgorithm.updateGameFeatures(gameId);
    }
    
    @Override
    public void trainModel() {
        // 训练两个算法
        collaborativeFilteringAlgorithm.trainModel();
        contentBasedAlgorithm.trainModel();
        
        // 根据评估结果动态调整权重
        adjustWeights();
    }
    
    @Override
    public RecommendMetrics evaluateModel() {
        // 1. 评估两个算法
        RecommendMetrics collaborativeMetrics = collaborativeFilteringAlgorithm.evaluateModel();
        RecommendMetrics contentBasedMetrics = contentBasedAlgorithm.evaluateModel();
        
        // 2. 加权合并评估指标
        return RecommendMetrics.builder()
                .precision(collaborativeWeight * collaborativeMetrics.getPrecision() + 
                          contentBasedWeight * contentBasedMetrics.getPrecision())
                .recall(collaborativeWeight * collaborativeMetrics.getRecall() + 
                       contentBasedWeight * contentBasedMetrics.getRecall())
                .mrr(collaborativeWeight * collaborativeMetrics.getMrr() + 
                     contentBasedWeight * contentBasedMetrics.getMrr())
                .ndcg(collaborativeWeight * collaborativeMetrics.getNdcg() + 
                      contentBasedWeight * contentBasedMetrics.getNdcg())
                .coverage(collaborativeWeight * collaborativeMetrics.getCoverage() + 
                         contentBasedWeight * contentBasedMetrics.getCoverage())
                .diversity(collaborativeWeight * collaborativeMetrics.getDiversity() + 
                          contentBasedWeight * contentBasedMetrics.getDiversity())
                .novelty(collaborativeWeight * collaborativeMetrics.getNovelty() + 
                        contentBasedWeight * contentBasedMetrics.getNovelty())
                .timeliness(collaborativeWeight * collaborativeMetrics.getTimeliness() + 
                           contentBasedWeight * contentBasedMetrics.getTimeliness())
                .coldStartPerformance(collaborativeWeight * collaborativeMetrics.getColdStartPerformance() + 
                                    contentBasedWeight * contentBasedMetrics.getColdStartPerformance())
                .build();
    }
    
    private void adjustWeights() {
        // 1. 获取两个算法的评估结果
        RecommendMetrics collaborativeMetrics = collaborativeFilteringAlgorithm.evaluateModel();
        RecommendMetrics contentBasedMetrics = contentBasedAlgorithm.evaluateModel();
        
        // 2. 计算两个算法的综合得分
        double collaborativeScore = calculateOverallScore(collaborativeMetrics);
        double contentBasedScore = calculateOverallScore(contentBasedMetrics);
        
        // 3. 根据得分调整权重
        double totalScore = collaborativeScore + contentBasedScore;
        if (totalScore > 0) {
            collaborativeWeight = collaborativeScore / totalScore;
            contentBasedWeight = contentBasedScore / totalScore;
        }
    }
    
    private double calculateOverallScore(RecommendMetrics metrics) {
        // 计算综合得分，可以根据实际需求调整各个指标的权重
        return metrics.getPrecision() * 0.3 +
               metrics.getRecall() * 0.3 +
               metrics.getMrr() * 0.1 +
               metrics.getNdcg() * 0.1 +
               metrics.getCoverage() * 0.05 +
               metrics.getDiversity() * 0.05 +
               metrics.getNovelty() * 0.05 +
               metrics.getTimeliness() * 0.05;
    }
} 