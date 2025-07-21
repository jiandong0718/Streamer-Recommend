package com.recommend.service;

import com.recommend.common.entity.UserProfile;
import java.util.List;

public interface UserProfileService {
    UserProfile getUserProfile(Long userId);
    void updateUserProfile(UserProfile userProfile);
    void updateUserTags(Long userId, List<String> tags);
    
    /**
     * 获取所有用户画像
     */
    List<UserProfile> getAllUserProfiles();
    
    /**
     * 创建用户画像
     */
    void createUserProfile(UserProfile userProfile);
    
    /**
     * 删除用户画像
     */
    void deleteUserProfile(Long userId);
} 