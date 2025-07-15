package com.recommend.service;

import com.recommend.common.entity.User;
import java.util.List;

public interface UserService {
    
    /**
     * 根据ID获取用户信息
     */
    User getUserById(Long userId);
    
    /**
     * 获取用户列表
     */
    List<User> getUserList();
    
    /**
     * 搜索用户
     */
    List<User> searchUsers(String keyword, Integer status);
    
    /**
     * 根据地区获取用户列表
     */
    List<User> getUsersByRegion(String region);
    
    /**
     * 根据状态获取用户列表
     */
    List<User> getUsersByStatus(Integer status);
    
    /**
     * 创建用户
     */
    void createUser(User user);
    
    /**
     * 更新用户信息
     */
    void updateUser(User user);
    
    /**
     * 更新用户状态
     */
    void updateUserStatus(Long userId, Integer status);
    
    /**
     * 更新用户最后登录时间
     */
    void updateLastLoginTime(Long userId);
    
    /**
     * 删除用户
     */
    void deleteUser(Long userId);
    
    /**
     * 获取用户画像
     */
    User getUserProfile(Long userId);
    
    /**
     * 获取用户标签
     */
    List<String> getUserTags(Long userId);
    
    /**
     * 获取用户行为
     */
    List<String> getUserBehaviors(Long userId);
    
    /**
     * 获取用户订单
     */
    List<String> getUserOrders(Long userId);
} 