package com.recommend.service.cache;

import com.recommend.common.entity.User;
import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.UserProfile;
import com.recommend.service.rank.RankedItem;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 推荐缓存服务接口
 * 
 * @author liujiandong
 */
public interface RecommendCacheService {

    /**
     * 缓存用户信息
     */
    void cacheUser(Long userId, User user);
    
    /**
     * 获取缓存的用户信息
     */
    User getCachedUser(Long userId);
    
    /**
     * 批量获取用户信息
     */
    Map<Long, User> getCachedUsers(Set<Long> userIds);
    
    /**
     * 缓存游戏大师信息
     */
    void cacheGameMaster(Long masterId, GameMaster gameMaster);
    
    /**
     * 获取缓存的游戏大师信息
     */
    GameMaster getCachedGameMaster(Long masterId);
    
    /**
     * 批量获取游戏大师信息
     */
    Map<Long, GameMaster> getCachedGameMasters(Set<Long> masterIds);
    
    /**
     * 缓存用户画像
     */
    void cacheUserProfile(Long userId, UserProfile userProfile);
    
    /**
     * 获取缓存的用户画像
     */
    UserProfile getCachedUserProfile(Long userId);
    
    /**
     * 缓存推荐结果
     */
    void cacheRecommendResult(Long userId, String scene, List<RankedItem> items);
    
    /**
     * 获取缓存的推荐结果
     */
    List<RankedItem> getCachedRecommendResult(Long userId, String scene);
    
    /**
     * 缓存用户偏好
     */
    void cacheUserPreference(Long userId, Map<String, Object> preferences);
    
    /**
     * 获取用户偏好
     */
    Map<String, Object> getCachedUserPreference(Long userId);
    
    /**
     * 缓存相似度计算结果
     */
    void cacheSimilarity(String key, Double similarity);
    
    /**
     * 获取相似度计算结果
     */
    Double getCachedSimilarity(String key);
    
    /**
     * 缓存热门推荐
     */
    void cacheHotRecommend(String category, List<RankedItem> items);
    
    /**
     * 获取热门推荐
     */
    List<RankedItem> getCachedHotRecommend(String category);
    
    /**
     * 缓存用户行为数据
     */
    void cacheUserBehavior(Long userId, String behaviorType, Object data);
    
    /**
     * 获取用户行为数据
     */
    Object getCachedUserBehavior(Long userId, String behaviorType);
    
    /**
     * 删除用户相关缓存
     */
    void evictUserCache(Long userId);
    
    /**
     * 删除游戏大师相关缓存
     */
    void evictGameMasterCache(Long masterId);
    
    /**
     * 清除所有缓存
     */
    void clearAllCache();
} 