package com.recommend.service.impl;

import com.recommend.service.RecommendationService;
import com.recommend.service.RecommendService;
import com.recommend.service.UserBehaviorService;
import com.recommend.service.UserProfileService;
import com.recommend.common.dto.RecommendRequest;
import com.recommend.common.dto.RecommendResponse;
import com.recommend.common.entity.UserBehavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Date;

/**
 * @author liujiandong
 */
@Service
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {
    
    @Autowired
    private RecommendService recommendService;
    
    @Autowired
    private UserBehaviorService userBehaviorService;
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Override
    public RecommendResponse recommendGameMasters(RecommendRequest request) {
        log.info("推荐游戏主，用户ID: {}", request.getUserId());
        return recommendService.recommendGameMasters(request);
    }
    
    @Override
    public RecommendResponse recommendGames(RecommendRequest request) {
        log.info("推荐游戏，用户ID: {}", request.getUserId());
        return recommendService.recommendGames(request);
    }
    
    @Override
    public RecommendResponse getPersonalizedRecommendations(RecommendRequest request) {
        log.info("获取个性化推荐，用户ID: {}", request.getUserId());
        return recommendService.getPersonalizedRecommendations(request);
    }
    
    @Override
    public RecommendResponse getHotRecommendations(Long userId, Integer count) {
        log.info("获取热门推荐，用户ID: {}", userId);
        return recommendService.getHotRecommendations(userId, count);
    }
    
    @Override
    public RecommendResponse getNewcomerRecommendations(Long userId, Integer count) {
        log.info("获取新人推荐，用户ID: {}", userId);
        return recommendService.getNewcomerRecommendations(userId, count);
    }
    
    @Override
    public RecommendResponse getSimilarUserRecommendations(Long userId, Integer count) {
        log.info("获取相似用户推荐，用户ID: {}", userId);
        return recommendService.getSimilarUserRecommendations(userId, count);
    }
    
    @Override
    public void recordUserBehavior(Long userId, Integer type, Long targetId, Integer targetType, String content) {
        log.info("记录用户行为，用户ID: {}, 类型: {}, 目标ID: {}", userId, type, targetId);
        
        UserBehavior userBehavior = new UserBehavior();
        userBehavior.setUserId(userId);
        userBehavior.setType(type.toString());
        userBehavior.setTargetId(targetId);
        userBehavior.setTargetType(targetType.toString());
        userBehavior.setContent(content);
        userBehavior.setCreateTime(new Date());
        
        userBehaviorService.addUserBehavior(userBehavior);
        
        // 同时调用推荐服务记录行为
        recommendService.recordUserBehavior(userId, type, targetId, targetType, content);
    }
    
    @Override
    public void updateUserPreferences(Long userId, List<String> tags, String region, String gender) {
        log.info("更新用户偏好，用户ID: {}", userId);
        recommendService.updateUserPreferences(userId, tags, region, gender);
    }
    
    @Override
    public String getRecommendationExplanation(Long userId, Long masterId) {
        log.info("获取推荐解释，用户ID: {}, 主播ID: {}", userId, masterId);
        return recommendService.getRecommendationExplanation(userId, masterId);
    }
    
    @Override
    public void refreshUserFeatures(Long userId) {
        log.info("刷新用户特征，用户ID: {}", userId);
        recommendService.refreshUserFeatures(userId);
    }
    
    @Override
    public void refreshMasterFeatures(Long masterId) {
        log.info("刷新主播特征，主播ID: {}", masterId);
        recommendService.refreshMasterFeatures(masterId);
    }
    
    @Override
    public void trainRecommendationModel() {
        log.info("训练推荐模型");
        recommendService.trainRecommendationModel();
    }
    
    @Override
    public void evaluateRecommendationModel() {
        log.info("评估推荐模型");
        recommendService.evaluateRecommendationModel();
    }
} 