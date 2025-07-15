package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.StreamerCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StreamerCategoryMapper extends BaseMapper<StreamerCategory> {
    
    /**
     * 根据主播ID查询分类列表
     */
    List<StreamerCategory> selectByStreamerId(@Param("streamerId") Long streamerId);
    
    /**
     * 根据分类ID查询主播列表
     */
    List<StreamerCategory> selectByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 根据评分范围查询
     */
    List<StreamerCategory> selectByScoreRange(@Param("minScore") Double minScore, @Param("maxScore") Double maxScore);
    
    /**
     * 根据直播场次范围查询
     */
    List<StreamerCategory> selectByStreamCountRange(@Param("minStreamCount") Integer minStreamCount, @Param("maxStreamCount") Integer maxStreamCount);
    
    /**
     * 更新主播分类评分
     */
    void updateScore(@Param("streamerId") Long streamerId, @Param("categoryId") Long categoryId, @Param("score") Double score);
    
    /**
     * 更新主播分类直播场次
     */
    void updateStreamCount(@Param("streamerId") Long streamerId, @Param("categoryId") Long categoryId, @Param("streamCount") Integer streamCount);
    
    /**
     * 删除主播分类关联
     */
    void deleteByStreamerAndCategory(@Param("streamerId") Long streamerId, @Param("categoryId") Long categoryId);
    
    /**
     * 批量删除主播分类
     */
    void batchDeleteByStreamerAndCategories(@Param("streamerId") Long streamerId, @Param("categoryIds") List<Long> categoryIds);
} 