package com.recommend.service;

import com.recommend.common.dto.RecommendRequest;
import com.recommend.common.dto.RecommendResponse;
import java.util.List;

public interface RecommendationService {
    
    /**
     * 推荐游戏主
     */
    RecommendResponse recommendGameMasters(RecommendRequest request);
    
    /**
     * 推荐游戏
     */
    RecommendResponse recommendGames(RecommendRequest request);
    
    /**
     * 获取个性化推荐
     */
    RecommendResponse getPersonalizedRecommendations(RecommendRequest request);
    
    /**
     * 获取热门推荐
     */
    RecommendResponse getHotRecommendations(Long userId, Integer count);
    
    /**
     * 获取新人推荐（冷启动）
     */
    RecommendResponse getNewcomerRecommendations(Long userId, Integer count);
    
    /**
     * 获取相似用户推荐
     */
    RecommendResponse getSimilarUserRecommendations(Long userId, Integer count);
    
    /**
     * 记录用户行为
     */
    void recordUserBehavior(Long userId, Integer type, Long targetId, Integer targetType, String content);
    
    /**
     * 更新用户偏好
     */
    void updateUserPreferences(Long userId, List<String> tags, String region, String gender);
    
    /**
     * 获取推荐解释
     */
    String getRecommendationExplanation(Long userId, Long masterId);
    
    /**
     * 刷新用户特征
     */
    void refreshUserFeatures(Long userId);
    
    /**
     * 刷新游戏主特征
     */
    void refreshMasterFeatures(Long masterId);
    
    /**
     * 训练推荐模型
     */
    void trainRecommendationModel();
    
    /**
     * 评估推荐模型
     */
    void evaluateRecommendationModel();
} 