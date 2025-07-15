package com.recommend.service;

import com.recommend.common.dto.RecommendRequest;
import com.recommend.common.dto.RecommendResponse;

/**
 * 主播搜索服务接口
 * 
 * @author liujiandong
 */
public interface StreamerSearchService {
    
    /**
     * 搜索主播列表
     */
    RecommendResponse searchStreamers(RecommendRequest request);
    
    /**
     * 技能匹配搜索
     */
    RecommendResponse skillMatchSearch(RecommendRequest request);
    
    /**
     * 新用户推荐
     */
    RecommendResponse newbieRecommend(RecommendRequest request);
    
    /**
     * 热门推荐
     */
    RecommendResponse hotRecommend(RecommendRequest request);
    
    /**
     * 个性化推荐
     */
    RecommendResponse personalRecommend(RecommendRequest request);
} 