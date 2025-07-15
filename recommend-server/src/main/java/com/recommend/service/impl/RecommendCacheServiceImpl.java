package com.recommend.service.impl;

import com.recommend.service.RecommendCacheService;
import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.Game;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 推荐缓存服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendCacheServiceImpl implements RecommendCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String GAME_MASTER_RECOMMEND_KEY = "recommend:game_master:";
    private static final String GAME_RECOMMEND_KEY = "recommend:game:";
    private static final String USER_FEATURES_KEY = "features:user:";
    private static final String MASTER_FEATURES_KEY = "features:master:";
    private static final String GAME_FEATURES_KEY = "features:game:";
    private static final int DEFAULT_EXPIRE_TIME = 300; // 5分钟

    @Override
    public List<GameMaster> getCachedGameMasterRecommendations(Long userId, Long gameId) {
        String key = GAME_MASTER_RECOMMEND_KEY + userId + ":" + gameId;
        String cachedValue = redisTemplate.opsForValue().get(key);
        
        if (cachedValue != null && cachedValue.length() > 0) {
            try {
                return JSON.parseObject(cachedValue, new TypeReference<List<GameMaster>>() {});
            } catch (Exception e) {
                log.error("解析游戏陪玩推荐缓存失败", e);
                redisTemplate.delete(key);
            }
        }
        return null;
    }

    @Override
    public void cacheGameMasterRecommendations(Long userId, Long gameId, List<GameMaster> recommendations) {
        String key = GAME_MASTER_RECOMMEND_KEY + userId + ":" + gameId;
        try {
            String value = JSON.toJSONString(recommendations);
            redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
            log.debug("缓存游戏陪玩推荐成功，用户ID: {}, 游戏ID: {}, 数量: {}", userId, gameId, recommendations.size());
        } catch (Exception e) {
            log.error("缓存游戏陪玩推荐失败", e);
        }
    }

    @Override
    public List<Game> getCachedGameRecommendations(Long userId) {
        String key = GAME_RECOMMEND_KEY + userId;
        String cachedValue = redisTemplate.opsForValue().get(key);
        
        if (cachedValue != null && cachedValue.length() > 0) {
            try {
                return JSON.parseObject(cachedValue, new TypeReference<List<Game>>() {});
            } catch (Exception e) {
                log.error("解析游戏推荐缓存失败", e);
                redisTemplate.delete(key);
            }
        }
        return null;
    }

    @Override
    public void cacheGameRecommendations(Long userId, List<Game> recommendations) {
        String key = GAME_RECOMMEND_KEY + userId;
        try {
            String value = JSON.toJSONString(recommendations);
            redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
            log.debug("缓存游戏推荐成功，用户ID: {}, 数量: {}", userId, recommendations.size());
        } catch (Exception e) {
            log.error("缓存游戏推荐失败", e);
        }
    }

    @Override
    public Object getUserFeatures(Long userId) {
        String key = USER_FEATURES_KEY + userId;
        String cachedValue = redisTemplate.opsForValue().get(key);
        
        if (cachedValue != null && cachedValue.length() > 0) {
            try {
                return JSON.parseObject(cachedValue, Object.class);
            } catch (Exception e) {
                log.error("解析用户特征缓存失败", e);
                redisTemplate.delete(key);
            }
        }
        return null;
    }

    @Override
    public void cacheUserFeatures(Long userId, Object features) {
        String key = USER_FEATURES_KEY + userId;
        try {
            String value = JSON.toJSONString(features);
            redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE_TIME * 2, TimeUnit.SECONDS);
            log.debug("缓存用户特征成功，用户ID: {}", userId);
        } catch (Exception e) {
            log.error("缓存用户特征失败", e);
        }
    }

    @Override
    public Object getMasterFeatures(Long masterId) {
        String key = MASTER_FEATURES_KEY + masterId;
        String cachedValue = redisTemplate.opsForValue().get(key);
        
        if (cachedValue != null && cachedValue.length() > 0) {
            try {
                return JSON.parseObject(cachedValue, Object.class);
            } catch (Exception e) {
                log.error("解析游戏陪玩特征缓存失败", e);
                redisTemplate.delete(key);
            }
        }
        return null;
    }

    @Override
    public void cacheMasterFeatures(Long masterId, Object features) {
        String key = MASTER_FEATURES_KEY + masterId;
        try {
            String value = JSON.toJSONString(features);
            redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE_TIME * 2, TimeUnit.SECONDS);
            log.debug("缓存游戏陪玩特征成功，陪玩ID: {}", masterId);
        } catch (Exception e) {
            log.error("缓存游戏陪玩特征失败", e);
        }
    }

    @Override
    public Object getGameFeatures(Long gameId) {
        String key = GAME_FEATURES_KEY + gameId;
        String cachedValue = redisTemplate.opsForValue().get(key);
        
        if (cachedValue != null && cachedValue.length() > 0) {
            try {
                return JSON.parseObject(cachedValue, Object.class);
            } catch (Exception e) {
                log.error("解析游戏特征缓存失败", e);
                redisTemplate.delete(key);
            }
        }
        return null;
    }

    @Override
    public void cacheGameFeatures(Long gameId, Object features) {
        String key = GAME_FEATURES_KEY + gameId;
        try {
            String value = JSON.toJSONString(features);
            redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE_TIME * 2, TimeUnit.SECONDS);
            log.debug("缓存游戏特征成功，游戏ID: {}", gameId);
        } catch (Exception e) {
            log.error("缓存游戏特征失败", e);
        }
    }

    @Override
    public void clearUserCache(Long userId) {
        try {
            String userRecommendKey = GAME_MASTER_RECOMMEND_KEY + userId + ":*";
            String gameRecommendKey = GAME_RECOMMEND_KEY + userId;
            String userFeaturesKey = USER_FEATURES_KEY + userId;
            
            redisTemplate.delete(gameRecommendKey);
            redisTemplate.delete(userFeaturesKey);
            
            // 清除用户相关的推荐缓存
            Set<String> keysToDelete = redisTemplate.keys(userRecommendKey);
            if (keysToDelete != null) {
                redisTemplate.delete(keysToDelete);
            }
            
            log.debug("清除用户缓存成功，用户ID: {}", userId);
        } catch (Exception e) {
            log.error("清除用户缓存失败", e);
        }
    }

    @Override
    public void clearMasterCache(Long masterId) {
        try {
            String masterFeaturesKey = MASTER_FEATURES_KEY + masterId;
            redisTemplate.delete(masterFeaturesKey);
            log.debug("清除游戏陪玩缓存成功，陪玩ID: {}", masterId);
        } catch (Exception e) {
            log.error("清除游戏陪玩缓存失败", e);
        }
    }

    @Override
    public void clearGameCache(Long gameId) {
        try {
            String gameFeaturesKey = GAME_FEATURES_KEY + gameId;
            redisTemplate.delete(gameFeaturesKey);
            log.debug("清除游戏缓存成功，游戏ID: {}", gameId);
        } catch (Exception e) {
            log.error("清除游戏缓存失败", e);
        }
    }

    @Override
    public void clearAllCache() {
        try {
            Set<String> gameMasterKeys = redisTemplate.keys(GAME_MASTER_RECOMMEND_KEY + "*");
            Set<String> gameRecommendKeys = redisTemplate.keys(GAME_RECOMMEND_KEY + "*");
            Set<String> userFeaturesKeys = redisTemplate.keys(USER_FEATURES_KEY + "*");
            Set<String> masterFeaturesKeys = redisTemplate.keys(MASTER_FEATURES_KEY + "*");
            Set<String> gameFeaturesKeys = redisTemplate.keys(GAME_FEATURES_KEY + "*");

            if (gameMasterKeys != null) {
                redisTemplate.delete(gameMasterKeys);
            }
            if (gameRecommendKeys != null) {
                redisTemplate.delete(gameRecommendKeys);
            }
            if (userFeaturesKeys != null) {
                redisTemplate.delete(userFeaturesKeys);
            }
            if (masterFeaturesKeys != null) {
                redisTemplate.delete(masterFeaturesKeys);
            }
            if (gameFeaturesKeys != null) {
                redisTemplate.delete(gameFeaturesKeys);
            }
            log.info("清除所有推荐缓存成功");
        } catch (Exception e) {
            log.error("清除所有推荐缓存失败", e);
        }
    }
} 