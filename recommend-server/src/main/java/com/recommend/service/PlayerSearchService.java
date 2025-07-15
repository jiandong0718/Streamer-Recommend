package com.recommend.service;

import com.recommend.common.dto.RecommendRequest;
import com.recommend.common.dto.RecommendResponse;

/**
 * 玩家搜索服务接口
 * 
 * @author liujiandong
 */
public interface PlayerSearchService {

    /**
     * 搜索游戏陪玩师列表
     */
    RecommendResponse searchPlaymateList(RecommendRequest request);

    /**
     * 根据技能搜索匹配的玩家
     */
    RecommendResponse searchBySkill(RecommendRequest request);

    /**
     * 获取新用户推荐
     */
    RecommendResponse getNewUserRecommend(RecommendRequest request);

    /**
     * 获取热门推荐
     */
    RecommendResponse getHotRecommend(RecommendRequest request);

    /**
     * 获取个性化推荐
     */
    RecommendResponse getPersonalizedRecommend(RecommendRequest request);
} 