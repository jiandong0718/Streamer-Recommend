package com.recommend.service.impl;

import com.recommend.common.dto.RecommendRequest;
import com.recommend.common.dto.RecommendResponse;
import com.recommend.service.StreamerSearchService;
import com.recommend.service.StreamerService;
import com.recommend.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 主播搜索服务实现类
 * 
 * @author liujiandong
 */
@Service
@Slf4j
public class StreamerSearchServiceImpl implements StreamerSearchService {
    
    @Autowired
    private StreamerService streamerService;
    
    @Autowired
    private RecommendService recommendService;
    
    @Override
    public RecommendResponse searchStreamers(RecommendRequest request) {
        log.info("搜索主播列表，请求参数: {}", request);
        
        try {
            // 调用推荐服务进行主播搜索
            return recommendService.getPersonalRecommend(request);
        } catch (Exception e) {
            log.error("搜索主播列表失败", e);
            return RecommendResponse.error("搜索失败: " + e.getMessage());
        }
    }
    
    @Override
    public RecommendResponse skillMatchSearch(RecommendRequest request) {
        log.info("技能匹配搜索，请求参数: {}", request);
        
        try {
            // 基于分类和技能等级进行匹配推荐
            return recommendService.getPersonalRecommend(request);
        } catch (Exception e) {
            log.error("技能匹配搜索失败", e);
            return RecommendResponse.error("搜索失败: " + e.getMessage());
        }
    }
    
    @Override
    public RecommendResponse newbieRecommend(RecommendRequest request) {
        log.info("新用户推荐，请求参数: {}", request);
        
        try {
            // 为新用户提供入门级推荐
            return recommendService.getHotRecommend(request);
        } catch (Exception e) {
            log.error("新用户推荐失败", e);
            return RecommendResponse.error("推荐失败: " + e.getMessage());
        }
    }
    
    @Override
    public RecommendResponse hotRecommend(RecommendRequest request) {
        log.info("热门推荐，请求参数: {}", request);
        
        try {
            // 获取热门主播推荐
            return recommendService.getHotRecommend(request);
        } catch (Exception e) {
            log.error("热门推荐失败", e);
            return RecommendResponse.error("推荐失败: " + e.getMessage());
        }
    }
    
    @Override
    public RecommendResponse personalRecommend(RecommendRequest request) {
        log.info("个性化推荐，请求参数: {}", request);
        
        try {
            // 获取个性化主播推荐
            return recommendService.getPersonalRecommend(request);
        } catch (Exception e) {
            log.error("个性化推荐失败", e);
            return RecommendResponse.error("推荐失败: " + e.getMessage());
        }
    }
} 