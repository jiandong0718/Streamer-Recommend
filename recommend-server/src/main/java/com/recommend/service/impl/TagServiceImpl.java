package com.recommend.service.impl;

import com.alibaba.fastjson.JSON;
import com.recommend.common.entity.GameMasterTag;
import com.recommend.common.entity.Tag;
import com.recommend.common.entity.UserTag;
import com.recommend.service.TagService;
import com.recommend.service.mapper.GameMasterTagMapper;
import com.recommend.service.mapper.TagMapper;
import com.recommend.service.mapper.UserTagMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserTagMapper userTagMapper;

    @Autowired
    private GameMasterTagMapper gameMasterTagMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String TAG_CACHE_KEY = "tag:";
    private static final String USER_TAG_CACHE_KEY = "user:tag:";
    private static final String MASTER_TAG_CACHE_KEY = "master:tag:";

    @Override
    public Tag getTagById(Long tagId) {
        // 先从缓存获取
        String cacheKey = TAG_CACHE_KEY + tagId;
        String tagJson = redisTemplate.opsForValue().get(cacheKey);

        if (tagJson != null) {
            return JSON.parseObject(tagJson, Tag.class);
        }

        // 缓存未命中，从数据库获取
        Tag tag = tagMapper.selectById(tagId);
        if (tag != null) {
            // 写入缓存
            redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(tag));
        }

        return tag;
    }

    @Override
    public List<Tag> getTagList() {
        return tagMapper.selectList(null);
    }

    @Override
    public List<Tag> searchTags(String keyword, Integer type, String category) {
        return tagMapper.searchTags(keyword, type, category);
    }

    @Override
    public List<Tag> getTagsByTypeAndCategory(Integer type, String category) {
        return tagMapper.selectByTypeAndCategory(type, category);
    }

    @Override
    public List<Tag> getTagsByStatus(Integer status) {
        return tagMapper.selectByStatus(status);
    }

    @Override
    public List<Tag> getTagsByWeightRange(Double minWeight, Double maxWeight) {
        return tagMapper.selectByWeightRange(minWeight, maxWeight);
    }

    @Override
    @Transactional
    public void createTag(Tag tag) {
        tag.setCreateTime(new Date());
        tag.setUpdateTime(new Date());
        tagMapper.insert(tag);
    }

    @Override
    @Transactional
    public void updateTag(Tag tag) {
        tag.setUpdateTime(new Date());
        tagMapper.updateById(tag);
        // 清除缓存
        redisTemplate.delete(TAG_CACHE_KEY + tag.getId());
    }

    @Override
    @Transactional
    public void updateTagStatus(Long tagId, Integer status) {
        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setStatus(status);
        tag.setUpdateTime(new Date());
        tagMapper.updateById(tag);
        // 清除缓存
        redisTemplate.delete(TAG_CACHE_KEY + tagId);
    }

    @Override
    @Transactional
    public void updateTagWeight(Long tagId, Double weight) {
        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setWeight(new java.math.BigDecimal(weight));
        tag.setUpdateTime(new Date());
        tagMapper.updateById(tag);
        // 清除缓存
        redisTemplate.delete(TAG_CACHE_KEY + tagId);
    }

    @Override
    @Transactional
    public void deleteTag(Long tagId) {
        tagMapper.deleteById(tagId);
        // 清除缓存
        redisTemplate.delete(TAG_CACHE_KEY + tagId);
    }

    @Override
    public List<Tag> listTags(Integer type, String category) {
        return tagMapper.selectByTypeAndCategory(type, category);
    }

    @Override
    @Transactional
    public void addUserTag(UserTag userTag) {
        userTag.setCreateTime(new Date());
        userTag.setUpdateTime(new Date());
        userTagMapper.insert(userTag);
    }

    @Override
    @Transactional
    public void addGameMasterTag(GameMasterTag gameMasterTag) {
        gameMasterTag.setCreateTime(new Date());
        gameMasterTag.setUpdateTime(new Date());
        gameMasterTagMapper.insert(gameMasterTag);
    }

    @Override
    @Transactional
    public void removeUserTag(Long userId, Long tagId) {
        userTagMapper.deleteByUserIdAndTagId(userId, tagId);
    }

    @Override
    @Transactional
    public void removeGameMasterTag(Long masterId, Long tagId) {
        gameMasterTagMapper.deleteByMasterIdAndTagId(masterId, tagId);
    }

    @Override
    public List<UserTag> getUserTags(Long userId) {
        return userTagMapper.selectByUserId(userId);
    }

    @Override
    public List<GameMasterTag> getGameMasterTags(Long masterId) {
        return gameMasterTagMapper.selectByMasterId(masterId);
    }

    @Override
    public List<String> getTagUsers(Long tagId) {
        // TODO: 实现获取标签用户列表的逻辑
        return new ArrayList<>();
    }

    @Override
    public List<String> getTagMasters(Long tagId) {
        // TODO: 实现获取标签陪玩师列表的逻辑
        return new ArrayList<>();
    }

    @Override
    public Double getTagWeight(Long tagId) {
        Tag tag = getTagById(tagId);
        return tag != null ? tag.getWeight().doubleValue() : 0.0;
    }

    @Override
    public List<Tag> getSimilarTags(Long tagId, Integer limit) {
        // TODO: 实现获取相似标签的逻辑
        return new ArrayList<>();
    }

    @Override
    public Map<Long, Double> calculateTagSimilarity(Long tagId, List<Long> targetTagIds) {
        // TODO: 实现计算标签相似度的逻辑
        return new HashMap<>();
    }

    @Override
    public List<Tag> getHotTags(Integer type, Integer limit) {
        // TODO: 实现获取热门标签的逻辑
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getTagStatistics(Long tagId) {
        // TODO: 实现获取标签统计信息的逻辑
        return new HashMap<>();
    }

    @Override
    public Map<Long, Double> calculateUserTagWeights(Long userId) {
        // TODO: 实现计算用户标签权重的逻辑
        return new HashMap<>();
    }

    @Override
    public Map<Long, Double> calculateGameMasterTagWeights(Long masterId) {
        // TODO: 实现计算陪玩师标签权重的逻辑
        return new HashMap<>();
    }

    @Override
    @Transactional
    public void batchUpdateUserTags(Long userId, List<UserTag> userTags) {
        // TODO: 实现批量更新用户标签的逻辑
        for (UserTag userTag : userTags) {
            userTag.setUserId(userId);
            userTag.setCreateTime(new Date());
            userTag.setUpdateTime(new Date());
            userTagMapper.insert(userTag);
        }
    }

    @Override
    @Transactional
    public void batchUpdateGameMasterTags(Long masterId, List<GameMasterTag> gameMasterTags) {
        // TODO: 实现批量更新陪玩师标签的逻辑
        for (GameMasterTag masterTag : gameMasterTags) {
            masterTag.setMasterId(masterId);
            masterTag.setCreateTime(new Date());
            masterTag.setUpdateTime(new Date());
            gameMasterTagMapper.insert(masterTag);
        }
    }

    @Override
    public List<Long> getGameTags(Long gameId) {
        return Collections.emptyList();
    }

    @Override
    public int getTagUsageCount(Long tagId) {
        return 0;
    }

    @Override
    public Collection<Object> getAllTags() {
        return Collections.emptyList();
    }
} 