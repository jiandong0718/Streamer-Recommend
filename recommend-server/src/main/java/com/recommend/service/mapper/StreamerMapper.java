package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.Streamer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StreamerMapper extends BaseMapper<Streamer> {
    
    /**
     * 搜索主播
     */
    List<Streamer> searchStreamers(@Param("keyword") String keyword, @Param("status") Integer status);
    
    /**
     * 根据地区查询主播列表
     */
    List<Streamer> selectByRegion(@Param("region") String region);
    
    /**
     * 根据状态查询主播列表
     */
    List<Streamer> selectByStatus(@Param("status") Integer status);
    
    /**
     * 根据评分范围查询主播列表
     */
    List<Streamer> selectByScoreRange(@Param("minScore") Double minScore, @Param("maxScore") Double maxScore);
    
    /**
     * 根据分类查询主播列表
     */
    List<Streamer> selectByCategory(@Param("categoryId") Long categoryId);
    
    /**
     * 根据标签查询主播列表
     */
    List<Streamer> findByTags(@Param("tags") List<String> tags);
    
    /**
     * 更新主播评分
     */
    void updateScore(@Param("streamerId") Long streamerId, @Param("score") Double score);
    
    /**
     * 获取主播详细信息
     */
    Streamer selectStreamerProfile(@Param("streamerId") Long streamerId);
    
    /**
     * 获取主播标签
     */
    List<String> selectStreamerTags(@Param("streamerId") Long streamerId);
    
    /**
     * 获取主播分类
     */
    List<String> selectStreamerCategories(@Param("streamerId") Long streamerId);
    
    /**
     * 获取主播订单
     */
    List<String> selectStreamerOrders(@Param("streamerId") Long streamerId);
    
    /**
     * 推荐主播
     */
    List<Streamer> selectRecommendStreamers(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("limit") Integer limit);
} 