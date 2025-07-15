package com.recommend.service.impl;

import com.recommend.service.UserService;
import com.recommend.service.mapper.UserMapper;
import com.recommend.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }
    
    @Override
    public List<User> getUserList() {
        return userMapper.selectList(null);
    }
    
    @Override
    public List<User> searchUsers(String keyword, Integer status) {
        return userMapper.searchUsers(keyword, status);
    }
    
    @Override
    public List<User> getUsersByRegion(String region) {
        return userMapper.selectByRegion(region);
    }
    
    @Override
    public List<User> getUsersByStatus(Integer status) {
        return userMapper.selectByStatus(status);
    }
    
    @Override
    @Transactional
    public void createUser(User user) {
        userMapper.insert(user);
    }
    
    @Override
    @Transactional
    public void updateUser(User user) {
        userMapper.updateById(user);
    }
    
    @Override
    @Transactional
    public void updateUserStatus(Long userId, Integer status) {
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        userMapper.updateById(user);
    }
    
    @Override
    @Transactional
    public void updateLastLoginTime(Long userId) {
        userMapper.updateLastLoginTime(userId);
    }
    
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userMapper.deleteById(userId);
    }
    
    @Override
    public User getUserProfile(Long userId) {
        return userMapper.selectById(userId);
    }
    
    @Override
    public List<String> getUserTags(Long userId) {
        // TODO: 实现获取用户标签的逻辑
        return new ArrayList<>();
    }
    
    @Override
    public List<String> getUserBehaviors(Long userId) {
        // TODO: 实现获取用户行为的逻辑
        return new ArrayList<>();
    }
    
    @Override
    public List<String> getUserOrders(Long userId) {
        // TODO: 实现获取用户订单的逻辑
        return new ArrayList<>();
    }
} 