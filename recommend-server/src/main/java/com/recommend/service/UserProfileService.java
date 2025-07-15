package com.recommend.service;

import com.recommend.common.entity.UserProfile;
import java.util.List;

public interface UserProfileService {
    UserProfile getUserProfile(Long userId);
    void updateUserProfile(UserProfile userProfile);
    void updateUserTags(Long userId, List<String> tags);
} 