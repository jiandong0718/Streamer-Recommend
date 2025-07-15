package com.recommend.service.impl;

import com.recommend.service.StreamerService;
import com.recommend.service.mapper.StreamerMapper;
import com.recommend.common.entity.Streamer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
public class StreamerServiceImpl implements StreamerService {
    
    @Autowired
    private StreamerMapper streamerMapper;
    
    @Override
    public Streamer getStreamerById(Long streamerId) {
        return streamerMapper.selectById(streamerId);
    }
    
    @Override
    public List<Streamer> getStreamerList() {
        return streamerMapper.selectList(null);
    }
    
    @Override
    public List<Streamer> searchStreamers(String keyword, Integer status) {
        return streamerMapper.searchStreamers(keyword, status);
    }
    
    @Override
    public List<Streamer> getStreamersByRegion(String region) {
        return streamerMapper.selectByRegion(region);
    }
    
    @Override
    public List<Streamer> getStreamersByStatus(Integer status) {
        return streamerMapper.selectByStatus(status);
    }
    
    @Override
    public List<Streamer> getStreamersByScoreRange(Double minScore, Double maxScore) {
        return streamerMapper.selectByScoreRange(minScore, maxScore);
    }
    
    @Override
    public List<Streamer> getStreamersByCategory(Long categoryId) {
        return streamerMapper.selectByCategory(categoryId);
    }
    
    @Override
    @Transactional
    public void createStreamer(Streamer streamer) {
        streamerMapper.insert(streamer);
        log.info("创建主播成功，ID: {}", streamer.getId());
    }
    
    @Override
    @Transactional
    public void updateStreamer(Streamer streamer) {
        streamerMapper.updateById(streamer);
        log.info("更新主播信息成功，ID: {}", streamer.getId());
    }
    
    @Override
    @Transactional
    public void updateStreamerStatus(Long streamerId, Integer status) {
        Streamer streamer = new Streamer();
        streamer.setId(streamerId);
        streamer.setStatus(status);
        streamerMapper.updateById(streamer);
        log.info("更新主播状态成功，ID: {}, 状态: {}", streamerId, status);
    }
    
    @Override
    @Transactional
    public void updateStreamerScore(Long streamerId, Double score) {
        streamerMapper.updateScore(streamerId, score);
        log.info("更新主播评分成功，ID: {}, 评分: {}", streamerId, score);
    }
    
    @Override
    @Transactional
    public void deleteStreamer(Long streamerId) {
        streamerMapper.deleteById(streamerId);
        log.info("删除主播成功，ID: {}", streamerId);
    }
    
    @Override
    public Streamer getStreamerProfile(Long streamerId) {
        return streamerMapper.selectStreamerProfile(streamerId);
    }
    
    @Override
    public List<String> getStreamerTags(Long streamerId) {
        return streamerMapper.selectStreamerTags(streamerId);
    }
    
    @Override
    public List<String> getStreamerCategories(Long streamerId) {
        return streamerMapper.selectStreamerCategories(streamerId);
    }
    
    @Override
    public List<String> getStreamerOrders(Long streamerId) {
        return streamerMapper.selectStreamerOrders(streamerId);
    }
    
    @Override
    public List<Streamer> recommendStreamers(Long userId, Long categoryId, Integer limit) {
        return streamerMapper.selectRecommendStreamers(userId, categoryId, limit);
    }
} 