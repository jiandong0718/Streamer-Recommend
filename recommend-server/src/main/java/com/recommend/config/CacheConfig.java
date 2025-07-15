package com.recommend.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存配置
 * 支持多级缓存：本地缓存 + Redis缓存
 * 
 * @author liujiandong
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Redis缓存管理器配置
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 默认缓存配置
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        // 不同缓存的特定配置
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // 用户信息缓存 - 1小时
        cacheConfigurations.put("user", defaultConfig.entryTtl(Duration.ofHours(1)));
        
        // 游戏大师信息缓存 - 30分钟
        cacheConfigurations.put("gamemaster", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        // 推荐结果缓存 - 5分钟
        cacheConfigurations.put("recommend", defaultConfig.entryTtl(Duration.ofMinutes(5)));
        
        // 标签缓存 - 2小时
        cacheConfigurations.put("tag", defaultConfig.entryTtl(Duration.ofHours(2)));
        
        // 用户行为缓存 - 15分钟
        cacheConfigurations.put("behavior", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        
        // 热门推荐缓存 - 10分钟
        cacheConfigurations.put("hot", defaultConfig.entryTtl(Duration.ofMinutes(10)));
        
        // 用户偏好缓存 - 1小时
        cacheConfigurations.put("preference", defaultConfig.entryTtl(Duration.ofHours(1)));
        
        // 相似度计算缓存 - 30分钟
        cacheConfigurations.put("similarity", defaultConfig.entryTtl(Duration.ofMinutes(30)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
} 