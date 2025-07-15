package com.recommend.service;

import com.recommend.common.entity.Tag;
import com.recommend.common.entity.UserTag;
import com.recommend.common.entity.GameMasterTag;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 标签服务接口
 * 提供标签的增删改查、标签关联、标签权重计算等功能
 */
public interface TagService {
    
    /**
     * 根据ID获取标签信息
     */
    Tag getTagById(Long tagId);
    
    /**
     * 获取标签列表
     */
    List<Tag> getTagList();
    
    /**
     * 搜索标签
     */
    List<Tag> searchTags(String keyword, Integer type, String category);
    
    /**
     * 根据类型和分类获取标签列表
     */
    List<Tag> getTagsByTypeAndCategory(Integer type, String category);
    
    /**
     * 根据状态获取标签列表
     */
    List<Tag> getTagsByStatus(Integer status);
    
    /**
     * 根据权重范围获取标签列表
     */
    List<Tag> getTagsByWeightRange(Double minWeight, Double maxWeight);
    
    /**
     * 创建标签
     */
    void createTag(Tag tag);
    
    /**
     * 更新标签信息
     */
    void updateTag(Tag tag);
    
    /**
     * 更新标签状态
     */
    void updateTagStatus(Long tagId, Integer status);
    
    /**
     * 更新标签权重
     */
    void updateTagWeight(Long tagId, Double weight);
    
    /**
     * 删除标签
     */
    void deleteTag(Long tagId);
    
    /**
     * 获取标签列表
     */
    List<Tag> listTags(Integer type, String category);
    
    /**
     * 为用户添加标签
     */
    void addUserTag(UserTag userTag);
    
    /**
     * 为陪玩师添加标签
     */
    void addGameMasterTag(GameMasterTag gameMasterTag);
    
    /**
     * 移除用户标签
     */
    void removeUserTag(Long userId, Long tagId);
    
    /**
     * 移除陪玩师标签
     */
    void removeGameMasterTag(Long masterId, Long tagId);
    
    /**
     * 获取用户标签列表
     */
    List<UserTag> getUserTags(Long userId);
    
    /**
     * 获取陪玩师标签列表
     */
    List<GameMasterTag> getGameMasterTags(Long masterId);
    
    /**
     * 获取标签用户列表
     */
    List<String> getTagUsers(Long tagId);
    
    /**
     * 获取标签陪玩师列表
     */
    List<String> getTagMasters(Long tagId);
    
    /**
     * 获取标签权重
     */
    Double getTagWeight(Long tagId);
    
    /**
     * 获取相似标签
     */
    List<Tag> getSimilarTags(Long tagId, Integer limit);
    
    /**
     * 计算标签相似度
     */
    Map<Long, Double> calculateTagSimilarity(Long tagId, List<Long> targetTagIds);
    
    /**
     * 获取热门标签
     */
    List<Tag> getHotTags(Integer type, Integer limit);
    
    /**
     * 获取标签统计信息
     */
    Map<String, Object> getTagStatistics(Long tagId);
    
    /**
     * 计算用户标签权重
     * 基于用户行为、交互数据等计算标签权重
     */
    Map<Long, Double> calculateUserTagWeights(Long userId);
    
    /**
     * 计算陪玩师标签权重
     * 基于陪玩师表现、评价等计算标签权重
     */
    Map<Long, Double> calculateGameMasterTagWeights(Long masterId);
    
    /**
     * 批量更新用户标签
     */
    void batchUpdateUserTags(Long userId, List<UserTag> userTags);
    
    /**
     * 批量更新陪玩师标签
     */
    void batchUpdateGameMasterTags(Long masterId, List<GameMasterTag> gameMasterTags);

    List<Long> getGameTags(Long gameId);
    
    /**
     * 获取标签使用次数
     */
    int getTagUsageCount(Long tagId);

    Collection<Object> getAllTags();
}