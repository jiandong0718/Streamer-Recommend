package com.recommend.service;

import com.recommend.common.entity.StreamerCategory;
import java.util.List;

/**
 * @author liujiandong
 */
public interface StreamerCategoryService {
    
    /**
     * 根据主播ID获取分类列表
     */
    List<StreamerCategory> getStreamerCategoriesByStreamerId(Long streamerId);
    
    /**
     * 根据分类ID获取主播列表
     */
    List<StreamerCategory> getStreamerCategoriesByCategoryId(Long categoryId);
    
    /**
     * 根据评分范围获取主播分类列表
     */
    List<StreamerCategory> getStreamerCategoriesByScoreRange(Double minScore, Double maxScore);
    
    /**
     * 根据直播场次范围获取主播分类列表
     */
    List<StreamerCategory> getStreamerCategoriesByStreamCountRange(Integer minStreamCount, Integer maxStreamCount);
    
    /**
     * 添加主播分类
     */
    void addStreamerCategory(StreamerCategory streamerCategory);
    
    /**
     * 更新主播分类评分
     */
    void updateStreamerCategoryScore(Long streamerId, Long categoryId, Double score);
    
    /**
     * 更新主播分类直播场次
     */
    void updateStreamerCategoryStreamCount(Long streamerId, Long categoryId, Integer streamCount);
    
    /**
     * 删除主播分类
     */
    void deleteStreamerCategory(Long streamerId, Long categoryId);
    
    /**
     * 批量添加主播分类
     */
    void batchAddStreamerCategories(List<StreamerCategory> streamerCategories);
    
    /**
     * 批量删除主播分类
     */
    void batchDeleteStreamerCategories(Long streamerId, List<Long> categoryIds);
} 