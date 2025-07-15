package com.recommend.service.impl;

import com.recommend.service.StreamerTagService;
import com.recommend.service.mapper.StreamerTagMapper;
import com.recommend.common.entity.StreamerTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

@Service
@Slf4j
public class StreamerTagServiceImpl implements StreamerTagService {
    
    @Autowired
    private StreamerTagMapper streamerTagMapper;
    
    @Override
    public List<StreamerTag> getStreamerTagsByStreamerId(Long streamerId) {
        return streamerTagMapper.selectByStreamerId(streamerId);
    }
    
    @Override
    public List<StreamerTag> getStreamerTagsByTagId(Long tagId) {
        return streamerTagMapper.selectByTagId(tagId);
    }
    
    @Override
    public List<StreamerTag> getStreamerTagsByWeightRange(BigDecimal minWeight, BigDecimal maxWeight) {
        return streamerTagMapper.selectByWeightRange(minWeight, maxWeight);
    }
    
    @Override
    @Transactional
    public void addStreamerTag(StreamerTag streamerTag) {
        streamerTag.setCreateTime(new Date());
        streamerTag.setUpdateTime(new Date());
        streamerTagMapper.insert(streamerTag);
        log.info("添加主播标签成功，主播ID: {}, 标签ID: {}", streamerTag.getStreamerId(), streamerTag.getTagId());
    }
    
    @Override
    @Transactional
    public void updateStreamerTagWeight(Long streamerId, Long tagId, BigDecimal weight) {
        streamerTagMapper.updateWeight(streamerId, tagId, weight);
        log.info("更新主播标签权重成功，主播ID: {}, 标签ID: {}, 权重: {}", streamerId, tagId, weight);
    }
    
    @Override
    @Transactional
    public void deleteStreamerTag(Long streamerId, Long tagId) {
        streamerTagMapper.deleteByStreamerAndTag(streamerId, tagId);
        log.info("删除主播标签成功，主播ID: {}, 标签ID: {}", streamerId, tagId);
    }
    
    @Override
    @Transactional
    public void batchAddStreamerTags(List<StreamerTag> streamerTags) {
        for (StreamerTag streamerTag : streamerTags) {
            streamerTag.setCreateTime(new Date());
            streamerTag.setUpdateTime(new Date());
            streamerTagMapper.insert(streamerTag);
        }
        log.info("批量添加主播标签成功，数量: {}", streamerTags.size());
    }
    
    @Override
    @Transactional
    public void batchDeleteStreamerTags(Long streamerId, List<Long> tagIds) {
        streamerTagMapper.batchDeleteByStreamerAndTags(streamerId, tagIds);
        log.info("批量删除主播标签成功，主播ID: {}, 标签数量: {}", streamerId, tagIds.size());
    }
    
    @Override
    public BigDecimal calculateTagWeight(Long streamerId, Long tagId) {
        BigDecimal weight = streamerTagMapper.selectTagWeight(streamerId, tagId);
        return weight != null ? weight : BigDecimal.ZERO;
    }
    
    @Override
    public Double calculateTagSimilarity(Long streamerId1, Long streamerId2) {
        Double similarity = streamerTagMapper.calculateTagSimilarity(streamerId1, streamerId2);
        return similarity != null ? similarity : 0.0;
    }
} 