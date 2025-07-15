package com.recommend.service;

import com.recommend.common.entity.Streamer;
import java.util.List;

public interface StreamerService {
    
    /**
     * 根据ID获取主播信息
     */
    Streamer getStreamerById(Long streamerId);
    
    /**
     * 获取主播列表
     */
    List<Streamer> getStreamerList();
    
    /**
     * 搜索主播
     */
    List<Streamer> searchStreamers(String keyword, Integer status);
    
    /**
     * 根据地区获取主播列表
     */
    List<Streamer> getStreamersByRegion(String region);
    
    /**
     * 根据状态获取主播列表
     */
    List<Streamer> getStreamersByStatus(Integer status);
    
    /**
     * 根据评分范围获取主播列表
     */
    List<Streamer> getStreamersByScoreRange(Double minScore, Double maxScore);
    
    /**
     * 根据分类获取主播列表
     */
    List<Streamer> getStreamersByCategory(Long categoryId);
    
    /**
     * 创建主播
     */
    void createStreamer(Streamer streamer);
    
    /**
     * 更新主播信息
     */
    void updateStreamer(Streamer streamer);
    
    /**
     * 更新主播状态
     */
    void updateStreamerStatus(Long streamerId, Integer status);
    
    /**
     * 更新主播评分
     */
    void updateStreamerScore(Long streamerId, Double score);
    
    /**
     * 删除主播
     */
    void deleteStreamer(Long streamerId);
    
    /**
     * 获取主播画像
     */
    Streamer getStreamerProfile(Long streamerId);
    
    /**
     * 获取主播标签
     */
    List<String> getStreamerTags(Long streamerId);
    
    /**
     * 获取主播分类
     */
    List<String> getStreamerCategories(Long streamerId);
    
    /**
     * 获取主播订单
     */
    List<String> getStreamerOrders(Long streamerId);
    
    /**
     * 推荐主播
     */
    List<Streamer> recommendStreamers(Long userId, Long categoryId, Integer limit);
} 