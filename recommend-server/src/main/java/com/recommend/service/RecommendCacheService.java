package com.recommend.service;

import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.Game;
import java.util.List;

public interface RecommendCacheService {
    
    /**
     * 获取缓存的游戏陪玩推荐结果
     *
     * @param userId 用户ID
     * @param gameId 游戏ID
     * @return 推荐结果列表
     */
    List<GameMaster> getCachedGameMasterRecommendations(Long userId, Long gameId);
    
    /**
     * 缓存游戏陪玩推荐结果
     *
     * @param userId 用户ID
     * @param gameId 游戏ID
     * @param recommendations 推荐结果列表
     */
    void cacheGameMasterRecommendations(Long userId, Long gameId, List<GameMaster> recommendations);
    
    /**
     * 获取缓存的游戏推荐结果
     *
     * @param userId 用户ID
     * @return 推荐结果列表
     */
    List<Game> getCachedGameRecommendations(Long userId);
    
    /**
     * 缓存游戏推荐结果
     *
     * @param userId 用户ID
     * @param recommendations 推荐结果列表
     */
    void cacheGameRecommendations(Long userId, List<Game> recommendations);
    
    /**
     * 获取缓存的用户特征
     *
     * @param userId 用户ID
     * @return 用户特征
     */
    Object getUserFeatures(Long userId);
    
    /**
     * 缓存用户特征
     *
     * @param userId 用户ID
     * @param features 用户特征
     */
    void cacheUserFeatures(Long userId, Object features);
    
    /**
     * 获取缓存的游戏陪玩特征
     *
     * @param masterId 游戏陪玩ID
     * @return 游戏陪玩特征
     */
    Object getMasterFeatures(Long masterId);
    
    /**
     * 缓存游戏陪玩特征
     *
     * @param masterId 游戏陪玩ID
     * @param features 游戏陪玩特征
     */
    void cacheMasterFeatures(Long masterId, Object features);
    
    /**
     * 获取缓存的游戏特征
     *
     * @param gameId 游戏ID
     * @return 游戏特征
     */
    Object getGameFeatures(Long gameId);
    
    /**
     * 缓存游戏特征
     *
     * @param gameId 游戏ID
     * @param features 游戏特征
     */
    void cacheGameFeatures(Long gameId, Object features);
    
    /**
     * 清除用户相关的所有缓存
     *
     * @param userId 用户ID
     */
    void clearUserCache(Long userId);
    
    /**
     * 清除游戏陪玩相关的所有缓存
     *
     * @param masterId 游戏陪玩ID
     */
    void clearMasterCache(Long masterId);
    
    /**
     * 清除游戏相关的所有缓存
     *
     * @param gameId 游戏ID
     */
    void clearGameCache(Long gameId);
    
    /**
     * 清除所有缓存
     */
    void clearAllCache();
} 