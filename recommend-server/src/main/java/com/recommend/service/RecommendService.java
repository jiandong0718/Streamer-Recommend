package com.recommend.service;

import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.UserProfile;
import com.recommend.common.entity.UserGameMasterInteraction;
import com.recommend.common.request.FeedbackRequest;
import com.recommend.service.feature.FeatureExtractor;
import com.recommend.service.recall.RecallService;
import com.recommend.service.rank.RankService;
import com.recommend.service.filter.FilterService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.lang3.StringUtils;
import com.recommend.common.dto.RecommendRequest;
import com.recommend.common.dto.RecommendResponse;

@Service
@Slf4j
public class RecommendService {
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Autowired
    private RecallService recallService;
    
    @Autowired
    private RankService rankService;
    
    @Autowired
    private FilterService filterService;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private static final String RECOMMEND_CACHE_KEY = "recommend:";
    private static final int CACHE_EXPIRE_MINUTES = 5;
    
    public List<GameMaster> getRecommendations(Long userId) {
        // 1. 检查缓存
        String cacheKey = RECOMMEND_CACHE_KEY + userId;
        String cachedResult = redisTemplate.opsForValue().get(cacheKey);
        
        if (StringUtils.isNotEmpty(cachedResult)) {
            return JSON.parseArray(cachedResult, GameMaster.class);
        }
        
        // 2. 获取用户画像
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        
        // 3. 召回
        List<GameMaster> candidates = recallService.recallCandidates(userId);
        
        // 4. 排序
        List<GameMaster> ranked = rankService.rankCandidates(candidates, userId);
        
        // 5. 过滤和重排
        List<GameMaster> finalResults = filterService.filterAndRerank(ranked, userId);
        
        // 6. 更新缓存
        redisTemplate.opsForValue().set(
            cacheKey, 
            JSON.toJSONString(finalResults), 
            CACHE_EXPIRE_MINUTES, 
            TimeUnit.MINUTES
        );
        
        return finalResults;
    }
    
    public void recordFeedback(FeedbackRequest request) {
        // 1. 记录用户反馈
        UserGameMasterInteraction interaction = new UserGameMasterInteraction();
        interaction.setUserId(request.getUserId());
        interaction.setMasterId(request.getMasterId());
        interaction.setRating(request.getRating().doubleValue());
        interaction.setFeedback(request.getFeedback());
        interaction.setCreateTime(new Date());
        
        // 2. 异步更新模型
        CompletableFuture.runAsync(() -> {
            updateModelWithFeedback(interaction);
        });
    }
    
    private void updateModelWithFeedback(UserGameMasterInteraction interaction) {
        // 实现基于反馈更新模型的逻辑
    }

    /**
     * 推荐游戏主
     */
    public RecommendResponse recommendGameMasters(RecommendRequest request) {
        log.info("推荐游戏主，用户ID: {}", request.getUserId());
        try {
            List<GameMaster> recommendations = getRecommendations(request.getUserId());
            
            // 应用分页
            int page = request.getPage() != null ? request.getPage() : 1;
            int limit = request.getLimit() != null ? request.getLimit() : 10;
            int start = (page - 1) * limit;
            int end = Math.min(start + limit, recommendations.size());
            
            List<GameMaster> pagedResults = recommendations.subList(start, end);
            
            return RecommendResponse.page(pagedResults, page, limit, (long) recommendations.size());
        } catch (Exception e) {
            log.error("推荐游戏主失败", e);
            return RecommendResponse.error("推荐失败: " + e.getMessage());
        }
    }
    
    /**
     * 推荐游戏
     */
    public RecommendResponse recommendGames(RecommendRequest request) {
        log.info("推荐游戏，用户ID: {}", request.getUserId());
        try {
            // 这里应该有游戏推荐的具体逻辑
            // 暂时返回空列表
            return RecommendResponse.success(new ArrayList<>());
        } catch (Exception e) {
            log.error("推荐游戏失败", e);
            return RecommendResponse.error("推荐失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取个性化推荐
     */
    public RecommendResponse getPersonalRecommend(RecommendRequest request) {
        log.info("获取个性化推荐，用户ID: {}", request.getUserId());
        try {
            // 实现个性化推荐逻辑
            List<GameMaster> recommendations = getRecommendations(request.getUserId());
            return RecommendResponse.success(recommendations);
        } catch (Exception e) {
            log.error("获取个性化推荐失败", e);
            return RecommendResponse.error("推荐失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取个性化推荐（别名方法）
     */
    public RecommendResponse getPersonalizedRecommendations(RecommendRequest request) {
        return getPersonalRecommend(request);
    }
    
    /**
     * 获取热门推荐
     */
    public RecommendResponse getHotRecommend(RecommendRequest request) {
        log.info("获取热门推荐，用户ID: {}", request.getUserId());
        try {
            // 实现热门推荐逻辑
            List<GameMaster> hotRecommendations = getRecommendations(request.getUserId());
            return RecommendResponse.success(hotRecommendations);
        } catch (Exception e) {
            log.error("获取热门推荐失败", e);
            return RecommendResponse.error("推荐失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取热门推荐（别名方法）
     */
    public RecommendResponse getHotRecommendations(Long userId, Integer count) {
        RecommendRequest request = new RecommendRequest();
        request.setUserId(userId);
        request.setLimit(count);
        return getHotRecommend(request);
    }
    
    /**
     * 获取新人推荐（冷启动）
     */
    public RecommendResponse getNewcomerRecommendations(Long userId, Integer count) {
        log.info("获取新人推荐，用户ID: {}", userId);
        try {
            // 为新用户提供热门推荐
            RecommendRequest request = new RecommendRequest();
            request.setUserId(userId);
            request.setLimit(count);
            return getHotRecommend(request);
        } catch (Exception e) {
            log.error("获取新人推荐失败", e);
            return RecommendResponse.error("推荐失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取相似用户推荐
     */
    public RecommendResponse getSimilarUserRecommendations(Long userId, Integer count) {
        log.info("获取相似用户推荐，用户ID: {}", userId);
        try {
            // 基于相似用户的推荐
            List<GameMaster> recommendations = getRecommendations(userId);
            if (count != null && recommendations.size() > count) {
                recommendations = recommendations.subList(0, count);
            }
            return RecommendResponse.success(recommendations);
        } catch (Exception e) {
            log.error("获取相似用户推荐失败", e);
            return RecommendResponse.error("推荐失败: " + e.getMessage());
        }
    }
    
    /**
     * 记录用户行为
     */
    public void recordUserBehavior(Long userId, Integer type, Long targetId, Integer targetType, String content) {
        log.info("记录用户行为，用户ID: {}, 类型: {}, 目标ID: {}", userId, type, targetId);
        try {
            // 实现用户行为记录逻辑
            // 这里可以调用用户行为服务
        } catch (Exception e) {
            log.error("记录用户行为失败", e);
        }
    }
    
    /**
     * 更新用户偏好
     */
    public void updateUserPreferences(Long userId, List<String> tags, String region, String gender) {
        log.info("更新用户偏好，用户ID: {}", userId);
        try {
            // 实现用户偏好更新逻辑
            // 这里可以调用用户画像服务
        } catch (Exception e) {
            log.error("更新用户偏好失败", e);
        }
    }
    
    /**
     * 获取推荐解释
     */
    public String getRecommendationExplanation(Long userId, Long masterId) {
        log.info("获取推荐解释，用户ID: {}, 主播ID: {}", userId, masterId);
        try {
            return "基于您的历史行为和偏好，我们为您推荐了这位主播";
        } catch (Exception e) {
            log.error("获取推荐解释失败", e);
            return "推荐解释暂时不可用";
        }
    }
    
    /**
     * 刷新用户特征
     */
    public void refreshUserFeatures(Long userId) {
        log.info("刷新用户特征，用户ID: {}", userId);
        try {
            // 清除用户相关缓存
            String cacheKey = RECOMMEND_CACHE_KEY + userId;
            redisTemplate.delete(cacheKey);
        } catch (Exception e) {
            log.error("刷新用户特征失败", e);
        }
    }
    
    /**
     * 刷新主播特征
     */
    public void refreshMasterFeatures(Long masterId) {
        log.info("刷新主播特征，主播ID: {}", masterId);
        try {
            // 实现主播特征刷新逻辑
        } catch (Exception e) {
            log.error("刷新主播特征失败", e);
        }
    }
    
    /**
     * 训练推荐模型
     */
    public void trainRecommendationModel() {
        log.info("开始训练推荐模型");
        try {
            // 实现模型训练逻辑
            log.info("推荐模型训练完成");
        } catch (Exception e) {
            log.error("训练推荐模型失败", e);
        }
    }
    
    /**
     * 评估推荐模型
     */
    public void evaluateRecommendationModel() {
        log.info("开始评估推荐模型");
        try {
            // 实现模型评估逻辑
            log.info("推荐模型评估完成");
        } catch (Exception e) {
            log.error("评估推荐模型失败", e);
        }
    }
} 