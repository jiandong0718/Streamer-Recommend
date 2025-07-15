package com.recommend.service;

import com.recommend.common.entity.UserTag;
import java.util.List;

public interface UserTagService {
    
    /**
     * 根据用户ID获取标签列表
     */
    List<UserTag> getUserTagsByUserId(Long userId);
    
    /**
     * 根据标签ID获取用户列表
     */
    List<UserTag> getUserTagsByTagId(Long tagId);
    
    /**
     * 根据权重范围获取用户标签列表
     */
    List<UserTag> getUserTagsByWeightRange(Double minWeight, Double maxWeight);
    
    /**
     * 添加用户标签
     */
    void addUserTag(UserTag userTag);
    
    /**
     * 更新用户标签权重
     */
    void updateUserTagWeight(Long userId, Long tagId, Double weight);
    
    /**
     * 删除用户标签
     */
    void deleteUserTag(Long userId, Long tagId);
    
    /**
     * 批量添加用户标签
     */
    void batchAddUserTags(List<UserTag> userTags);
    
    /**
     * 批量删除用户标签
     */
    void batchDeleteUserTags(Long userId, List<Long> tagIds);
} 