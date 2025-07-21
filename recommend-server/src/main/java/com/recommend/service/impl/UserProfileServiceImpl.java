package com.recommend.service.impl;

import com.recommend.service.UserProfileService;
import com.recommend.service.mapper.UserProfileMapper;
import com.recommend.common.entity.UserProfile;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

@Service
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    
    @Autowired
    private UserProfileMapper userProfileMapper;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private static final String PROFILE_CACHE_KEY = "user:profile:";
    
    @Override
    public UserProfile getUserProfile(Long userId) {
        // 先从缓存获取
        String cacheKey = PROFILE_CACHE_KEY + userId;
        String profileJson = redisTemplate.opsForValue().get(cacheKey);
        
        if (StringUtils.isNotEmpty(profileJson)) {
            return JSON.parseObject(profileJson, UserProfile.class);
        }
        
        // 缓存未命中，从数据库获取
        UserProfile profile = userProfileMapper.selectOne(
            new LambdaQueryWrapper<UserProfile>()
                .eq(UserProfile::getUserId, userId)
        );
        
        if (profile != null) {
            // 写入缓存
            redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(profile), 1, TimeUnit.HOURS);
        }
        
        return profile;
    }
    
    @Override
    public void updateUserProfile(UserProfile userProfile) {
        userProfile.setUpdateTime(new Date());
        userProfileMapper.updateById(userProfile);
        
        // 更新缓存
        String cacheKey = PROFILE_CACHE_KEY + userProfile.getUserId();
        redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(userProfile), 1, TimeUnit.HOURS);
    }
    
    @Override
    public void updateUserTags(Long userId, List<String> tags) {
        UserProfile profile = getUserProfile(userId);
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
            profile.setCreateTime(new Date());
        }
        
        profile.setTags(JSON.toJSONString(tags));
        updateUserProfile(profile);
    }
    
    @Override
    public List<UserProfile> getAllUserProfiles() {
        try {
            return userProfileMapper.selectList(null);
        } catch (Exception e) {
            log.error("获取所有用户画像失败", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public void createUserProfile(UserProfile userProfile) {
        try {
            userProfile.setCreateTime(new Date());
            userProfile.setUpdateTime(new Date());
            userProfileMapper.insert(userProfile);
            
            // 写入缓存
            String cacheKey = PROFILE_CACHE_KEY + userProfile.getUserId();
            redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(userProfile), 1, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("创建用户画像失败，用户ID: {}", userProfile.getUserId(), e);
            throw new RuntimeException("创建用户画像失败", e);
        }
    }
    
    @Override
    public void deleteUserProfile(Long userId) {
        try {
            userProfileMapper.delete(
                new LambdaQueryWrapper<UserProfile>()
                    .eq(UserProfile::getUserId, userId)
            );
            
            // 删除缓存
            String cacheKey = PROFILE_CACHE_KEY + userId;
            redisTemplate.delete(cacheKey);
        } catch (Exception e) {
            log.error("删除用户画像失败，用户ID: {}", userId, e);
            throw new RuntimeException("删除用户画像失败", e);
        }
    }
} 