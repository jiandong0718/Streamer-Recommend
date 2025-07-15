package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.StreamerTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface StreamerTagMapper extends BaseMapper<StreamerTag> {
    
    /**
     * 根据主播ID查询标签列表
     */
    List<StreamerTag> selectByStreamerId(@Param("streamerId") Long streamerId);
    
    /**
     * 根据标签ID查询主播列表
     */
    List<StreamerTag> selectByTagId(@Param("tagId") Long tagId);
    
    /**
     * 根据权重范围查询
     */
    List<StreamerTag> selectByWeightRange(@Param("minWeight") BigDecimal minWeight, @Param("maxWeight") BigDecimal maxWeight);
    
    /**
     * 更新主播标签权重
     */
    void updateWeight(@Param("streamerId") Long streamerId, @Param("tagId") Long tagId, @Param("weight") BigDecimal weight);
    
    /**
     * 删除主播标签关联
     */
    void deleteByStreamerAndTag(@Param("streamerId") Long streamerId, @Param("tagId") Long tagId);
    
    /**
     * 批量删除主播标签
     */
    void batchDeleteByStreamerAndTags(@Param("streamerId") Long streamerId, @Param("tagIds") List<Long> tagIds);
    
    /**
     * 获取主播标签权重
     */
    BigDecimal selectTagWeight(@Param("streamerId") Long streamerId, @Param("tagId") Long tagId);
    
    /**
     * 计算主播标签相似度
     */
    Double calculateTagSimilarity(@Param("streamerId1") Long streamerId1, @Param("streamerId2") Long streamerId2);
} 