package com.recommend.service.impl;

import com.recommend.service.UserTagService;
import com.recommend.service.mapper.UserTagMapper;
import com.recommend.common.entity.UserTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
public class UserTagServiceImpl implements UserTagService {
    
    @Autowired
    private UserTagMapper userTagMapper;
    
    @Override
    public List<UserTag> getUserTagsByUserId(Long userId) {
        return userTagMapper.selectByUserId(userId);
    }
    
    @Override
    public List<UserTag> getUserTagsByTagId(Long tagId) {
        return userTagMapper.selectByTagId(tagId);
    }
    
    @Override
    public List<UserTag> getUserTagsByWeightRange(Double minWeight, Double maxWeight) {
        return userTagMapper.selectByWeightRange(minWeight, maxWeight);
    }

    @Override
    public UserTag getUserTagByUserIdAndTagId(Long userId, Long tagId) {
        return null;
    }

    @Override
    @Transactional
    public void addUserTag(UserTag userTag) {
        userTagMapper.insert(userTag);
    }
    
    @Override
    @Transactional
    public void updateUserTagWeight(Long userId, Long tagId, Double weight) {
        userTagMapper.updateWeight(userId, tagId, weight);
    }
    
    @Override
    @Transactional
    public void deleteUserTag(Long userId, Long tagId) {
        userTagMapper.deleteByUserIdAndTagId(userId, tagId);
    }
    
    @Override
    @Transactional
    public void batchAddUserTags(List<UserTag> userTags) {
        for (UserTag userTag : userTags) {
            userTagMapper.insert(userTag);
        }
    }
    
    @Override
    @Transactional
    public void batchDeleteUserTags(Long userId, List<Long> tagIds) {
        for (Long tagId : tagIds) {
            userTagMapper.deleteByUserIdAndTagId(userId, tagId);
        }
    }
} 