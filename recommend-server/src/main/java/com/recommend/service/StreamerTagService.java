package com.recommend.service;

import com.recommend.common.entity.StreamerTag;
import java.math.BigDecimal;
import java.util.List;

/**
 * 主播标签服务接口
 * @author liujiandong
 */
public interface StreamerTagService {
    
    /**
     * 根据主播ID获取标签列表
     */
    List<StreamerTag> getStreamerTagsByStreamerId(Long streamerId);
    
    /**
     * 根据标签ID获取主播列表
     */
    List<StreamerTag> getStreamerTagsByTagId(Long tagId);
    
    /**
     * 根据权重范围获取主播标签列表
     */
    List<StreamerTag> getStreamerTagsByWeightRange(BigDecimal minWeight, BigDecimal maxWeight);
    
    /**
     * 添加主播标签
     */
    void addStreamerTag(StreamerTag streamerTag);
    
    /**
     * 更新主播标签权重
     */
    void updateStreamerTagWeight(Long streamerId, Long tagId, BigDecimal weight);
    
    /**
     * 删除主播标签
     */
    void deleteStreamerTag(Long streamerId, Long tagId);
    
    /**
     * 批量添加主播标签
     */
    void batchAddStreamerTags(List<StreamerTag> streamerTags);
    
    /**
     * 批量删除主播标签
     */
    void batchDeleteStreamerTags(Long streamerId, List<Long> tagIds);
    
    /**
     * 计算主播标签权重
     */
    BigDecimal calculateTagWeight(Long streamerId, Long tagId);
    
    /**
     * 获取主播标签相似度
     */
    Double calculateTagSimilarity(Long streamerId1, Long streamerId2);
} 