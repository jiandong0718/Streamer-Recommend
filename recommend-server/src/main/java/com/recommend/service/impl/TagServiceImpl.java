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
        try {
            List<UserTag> userTags = userTagMapper.selectByTagId(tagId);
            return userTags.stream()
                .map(userTag -> userTag.getUserId().toString())
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            log.error("获取标签用户列表失败，标签ID: {}", tagId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getTagMasters(Long tagId) {
        try {
            List<GameMasterTag> masterTags = gameMasterTagMapper.selectByTagId(tagId);
            return masterTags.stream()
                .map(masterTag -> masterTag.getMasterId().toString())
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            log.error("获取标签陪玩师列表失败，标签ID: {}", tagId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public Double getTagWeight(Long tagId) {
        Tag tag = getTagById(tagId);
        return tag != null ? tag.getWeight().doubleValue() : 0.0;
    }

    @Override
    public List<Tag> getSimilarTags(Long tagId, Integer limit) {
        try {
            Tag sourceTag = getTagById(tagId);
            if (sourceTag == null) {
                return new ArrayList<>();
            }
            
            // 获取同类型的标签
            List<Tag> candidateTags = getTagsByTypeAndCategory(sourceTag.getType(), sourceTag.getCategory());
            
            // 计算相似度并排序
            return candidateTags.stream()
                .filter(tag -> !tag.getId().equals(tagId)) // 排除自己
                .sorted((t1, t2) -> {
                    // 基于标签名称相似度和权重进行排序
                    double similarity1 = calculateNameSimilarity(sourceTag.getName(), t1.getName()) + 
                                       (t1.getWeight() != null ? t1.getWeight().doubleValue() : 0.0) * 0.1;
                    double similarity2 = calculateNameSimilarity(sourceTag.getName(), t2.getName()) + 
                                       (t2.getWeight() != null ? t2.getWeight().doubleValue() : 0.0) * 0.1;
                    return Double.compare(similarity2, similarity1);
                })
                .limit(limit != null ? limit : 10)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            log.error("获取相似标签失败，标签ID: {}", tagId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<Long, Double> calculateTagSimilarity(Long tagId, List<Long> targetTagIds) {
        Map<Long, Double> similarities = new HashMap<>();
        try {
            Tag sourceTag = getTagById(tagId);
            if (sourceTag == null) {
                return similarities;
            }
            
            for (Long targetTagId : targetTagIds) {
                Tag targetTag = getTagById(targetTagId);
                if (targetTag != null) {
                    double similarity = 0.0;
                    
                    // 名称相似度
                    similarity += calculateNameSimilarity(sourceTag.getName(), targetTag.getName()) * 0.6;
                    
                    // 类型匹配度
                    if (Objects.equals(sourceTag.getType(), targetTag.getType())) {
                        similarity += 0.2;
                    }
                    
                    // 分类匹配度
                    if (Objects.equals(sourceTag.getCategory(), targetTag.getCategory())) {
                        similarity += 0.2;
                    }
                    
                    similarities.put(targetTagId, similarity);
                }
            }
        } catch (Exception e) {
            log.error("计算标签相似度失败，标签ID: {}", tagId, e);
        }
        return similarities;
    }

    @Override
    public List<Tag> getHotTags(Integer type, Integer limit) {
        try {
            // 获取指定类型的标签，按权重和使用频率排序
            List<Tag> tags = type != null ? 
                tagMapper.selectByType(type) : 
                tagMapper.selectList(null);
            
            return tags.stream()
                .filter(tag -> tag.getStatus() != null && tag.getStatus() == 1) // 只返回启用的标签
                .sorted((t1, t2) -> {
                    // 按权重排序，权重高的排前面
                    double weight1 = t1.getWeight() != null ? t1.getWeight().doubleValue() : 0.0;
                    double weight2 = t2.getWeight() != null ? t2.getWeight().doubleValue() : 0.0;
                    return Double.compare(weight2, weight1);
                })
                .limit(limit != null ? limit : 20)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            log.error("获取热门标签失败，类型: {}", type, e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> getTagStatistics(Long tagId) {
        Map<String, Object> statistics = new HashMap<>();
        try {
            Tag tag = getTagById(tagId);
            if (tag == null) {
                return statistics;
            }
            
            // 基础信息
            statistics.put("tagId", tagId);
            statistics.put("tagName", tag.getName());
            statistics.put("tagType", tag.getType());
            statistics.put("tagCategory", tag.getCategory());
            statistics.put("weight", tag.getWeight());
            
            // 使用统计
            List<UserTag> userTags = userTagMapper.selectByTagId(tagId);
            List<GameMasterTag> masterTags = gameMasterTagMapper.selectByTagId(tagId);
            
            statistics.put("userCount", userTags.size());
            statistics.put("masterCount", masterTags.size());
            statistics.put("totalUsage", userTags.size() + masterTags.size());
            
            // 权重统计
            double avgUserWeight = userTags.stream()
                .mapToDouble(ut -> ut.getWeight() != null ? ut.getWeight().doubleValue() : 0.0)
                .average()
                .orElse(0.0);
            
            double avgMasterWeight = masterTags.stream()
                .mapToDouble(mt -> mt.getWeight() != null ? mt.getWeight().doubleValue() : 0.0)
                .average()
                .orElse(0.0);
            
            statistics.put("avgUserWeight", avgUserWeight);
            statistics.put("avgMasterWeight", avgMasterWeight);
            
        } catch (Exception e) {
            log.error("获取标签统计信息失败，标签ID: {}", tagId, e);
        }
        return statistics;
    }

    @Override
    public Map<Long, Double> calculateUserTagWeights(Long userId) {
        Map<Long, Double> weights = new HashMap<>();
        try {
            List<UserTag> userTags = getUserTags(userId);
            
            for (UserTag userTag : userTags) {
                double weight = 0.0;
                
                // 基础权重（用户设置的权重）
                if (userTag.getWeight() != null) {
                    weight += userTag.getWeight().doubleValue() * 0.5;
                }
                
                // 时间权重（最近使用的标签权重更高）
                if (userTag.getUpdateTime() != null) {
                    long daysSinceUpdate = (System.currentTimeMillis() - userTag.getUpdateTime().getTime()) / (1000 * 60 * 60 * 24);
                    double timeWeight = Math.max(0.1, 1.0 - daysSinceUpdate / 30.0); // 30天内的权重较高
                    weight += timeWeight * 0.3;
                }
                
                // 标签热度权重
                Tag tag = getTagById(userTag.getTagId());
                if (tag != null && tag.getWeight() != null) {
                    weight += tag.getWeight().doubleValue() / 10.0 * 0.2; // 标签本身的权重
                }
                
                weights.put(userTag.getTagId(), Math.min(1.0, weight));
            }
        } catch (Exception e) {
            log.error("计算用户标签权重失败，用户ID: {}", userId, e);
        }
        return weights;
    }

    @Override
    public Map<Long, Double> calculateGameMasterTagWeights(Long masterId) {
        Map<Long, Double> weights = new HashMap<>();
        try {
            List<GameMasterTag> masterTags = getGameMasterTags(masterId);
            
            for (GameMasterTag masterTag : masterTags) {
                double weight = 0.0;
                
                // 基础权重（陪玩师设置的权重）
                if (masterTag.getWeight() != null) {
                    weight += masterTag.getWeight().doubleValue() * 0.4;
                }
                
                // 专业程度权重（基于标签使用时长）
                if (masterTag.getCreateTime() != null) {
                    long daysSinceCreation = (System.currentTimeMillis() - masterTag.getCreateTime().getTime()) / (1000 * 60 * 60 * 24);
                    double experienceWeight = Math.min(1.0, daysSinceCreation / 90.0); // 90天达到最高经验权重
                    weight += experienceWeight * 0.3;
                }
                
                // 标签热度权重
                Tag tag = getTagById(masterTag.getTagId());
                if (tag != null && tag.getWeight() != null) {
                    weight += tag.getWeight().doubleValue() / 10.0 * 0.2;
                }
                
                // 市场需求权重（基于该标签的用户数量）
                List<String> tagUsers = getTagUsers(masterTag.getTagId());
                double demandWeight = Math.min(0.1, tagUsers.size() / 100.0); // 用户越多需求越大
                weight += demandWeight;
                
                weights.put(masterTag.getTagId(), Math.min(1.0, weight));
            }
        } catch (Exception e) {
            log.error("计算陪玩师标签权重失败，陪玩师ID: {}", masterId, e);
        }
        return weights;
    }

    @Override
    @Transactional
    public void batchUpdateUserTags(Long userId, List<UserTag> userTags) {
        try {
            // 先删除用户现有的标签
            userTagMapper.deleteByUserId(userId);
            
            // 批量插入新标签
            for (UserTag userTag : userTags) {
                userTag.setUserId(userId);
                userTag.setCreateTime(new Date());
                userTag.setUpdateTime(new Date());
                userTagMapper.insert(userTag);
            }
            
            // 清除相关缓存
            redisTemplate.delete(USER_TAG_CACHE_KEY + userId);
            
        } catch (Exception e) {
            log.error("批量更新用户标签失败，用户ID: {}", userId, e);
            throw new RuntimeException("批量更新用户标签失败", e);
        }
    }

    @Override
    @Transactional
    public void batchUpdateGameMasterTags(Long masterId, List<GameMasterTag> gameMasterTags) {
        try {
            // 先删除陪玩师现有的标签
            gameMasterTagMapper.deleteByMasterId(masterId);
            
            // 批量插入新标签
            for (GameMasterTag masterTag : gameMasterTags) {
                masterTag.setMasterId(masterId);
                masterTag.setCreateTime(new Date());
                masterTag.setUpdateTime(new Date());
                gameMasterTagMapper.insert(masterTag);
            }
            
            // 清除相关缓存
            redisTemplate.delete(MASTER_TAG_CACHE_KEY + masterId);
            
        } catch (Exception e) {
            log.error("批量更新陪玩师标签失败，陪玩师ID: {}", masterId, e);
            throw new RuntimeException("批量更新陪玩师标签失败", e);
        }
    }

    @Override
    public List<Long> getGameTags(Long gameId) {
        try {
            // 这里应该查询游戏标签关联表，暂时返回空列表
            // 实际实现需要创建GameTag实体和对应的Mapper
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("获取游戏标签失败，游戏ID: {}", gameId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public int getTagUsageCount(Long tagId) {
        try {
            List<UserTag> userTags = userTagMapper.selectByTagId(tagId);
            List<GameMasterTag> masterTags = gameMasterTagMapper.selectByTagId(tagId);
            return userTags.size() + masterTags.size();
        } catch (Exception e) {
            log.error("获取标签使用次数失败，标签ID: {}", tagId, e);
            return 0;
        }
    }

    @Override
    public Collection<Object> getAllTags() {
        try {
            List<Tag> tags = tagMapper.selectList(null);
            return new ArrayList<>(tags);
        } catch (Exception e) {
            log.error("获取所有标签失败", e);
            return Collections.emptyList();
        }
    }

    /**
     * 计算两个标签名称的相似度
     * 使用编辑距离算法计算相似度
     */
    private double calculateNameSimilarity(String name1, String name2) {
        if (name1 == null || name2 == null) {
            return 0.0;
        }
        
        if (name1.equals(name2)) {
            return 1.0;
        }
        
        // 计算编辑距离
        int editDistance = calculateEditDistance(name1, name2);
        int maxLength = Math.max(name1.length(), name2.length());
        
        if (maxLength == 0) {
            return 1.0;
        }
        
        // 相似度 = 1 - (编辑距离 / 最大长度)
        return 1.0 - (double) editDistance / maxLength;
    }

    /**
     * 计算两个字符串的编辑距离（Levenshtein距离）
     */
    private int calculateEditDistance(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        
        int[][] dp = new int[len1 + 1][len2 + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }
        
        // 动态规划计算编辑距离
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + 1
                    );
                }
            }
        }
        
        return dp[len1][len2];
    }
} 