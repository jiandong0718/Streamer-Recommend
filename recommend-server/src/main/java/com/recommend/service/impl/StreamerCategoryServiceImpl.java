package com.recommend.service.impl;

import com.recommend.common.entity.StreamerCategory;
import com.recommend.service.StreamerCategoryService;
import com.recommend.service.mapper.StreamerCategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreamerCategoryServiceImpl implements StreamerCategoryService {
    
    private final StreamerCategoryMapper streamerCategoryMapper;
    
    @Override
    public List<StreamerCategory> getStreamerCategoriesByStreamerId(Long streamerId) {
        return streamerCategoryMapper.selectByStreamerId(streamerId);
    }
    
    @Override
    public List<StreamerCategory> getStreamerCategoriesByCategoryId(Long categoryId) {
        return streamerCategoryMapper.selectByCategoryId(categoryId);
    }
    
    @Override
    public List<StreamerCategory> getStreamerCategoriesByScoreRange(Double minScore, Double maxScore) {
        return streamerCategoryMapper.selectByScoreRange(minScore, maxScore);
    }
    
    @Override
    public List<StreamerCategory> getStreamerCategoriesByStreamCountRange(Integer minStreamCount, Integer maxStreamCount) {
        return streamerCategoryMapper.selectByStreamCountRange(minStreamCount, maxStreamCount);
    }
    
    @Override
    public void addStreamerCategory(StreamerCategory streamerCategory) {
        streamerCategory.setCreateTime(new Date());
        streamerCategory.setUpdateTime(new Date());
        streamerCategoryMapper.insert(streamerCategory);
        log.info("添加主播分类成功，主播ID: {}, 分类ID: {}", streamerCategory.getStreamerId(), streamerCategory.getCategoryId());
    }
    
    @Override
    public void updateStreamerCategoryScore(Long streamerId, Long categoryId, Double score) {
        streamerCategoryMapper.updateScore(streamerId, categoryId, score);
        log.info("更新主播分类评分成功，主播ID: {}, 分类ID: {}, 评分: {}", streamerId, categoryId, score);
    }
    
    @Override
    public void updateStreamerCategoryStreamCount(Long streamerId, Long categoryId, Integer streamCount) {
        streamerCategoryMapper.updateStreamCount(streamerId, categoryId, streamCount);
        log.info("更新主播分类直播场次成功，主播ID: {}, 分类ID: {}, 场次: {}", streamerId, categoryId, streamCount);
    }
    
    @Override
    public void deleteStreamerCategory(Long streamerId, Long categoryId) {
        streamerCategoryMapper.deleteByStreamerAndCategory(streamerId, categoryId);
        log.info("删除主播分类成功，主播ID: {}, 分类ID: {}", streamerId, categoryId);
    }
    
    @Override
    public void batchAddStreamerCategories(List<StreamerCategory> streamerCategories) {
        for (StreamerCategory streamerCategory : streamerCategories) {
            streamerCategory.setCreateTime(new Date());
            streamerCategory.setUpdateTime(new Date());
            streamerCategoryMapper.insert(streamerCategory);
        }
        log.info("批量添加主播分类成功，数量: {}", streamerCategories.size());
    }
    
    @Override
    public void batchDeleteStreamerCategories(Long streamerId, List<Long> categoryIds) {
        streamerCategoryMapper.batchDeleteByStreamerAndCategories(streamerId, categoryIds);
        log.info("批量删除主播分类成功，主播ID: {}, 分类数量: {}", streamerId, categoryIds.size());
    }
} 