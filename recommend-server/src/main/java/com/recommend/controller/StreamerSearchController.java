package com.recommend.controller;

import com.recommend.common.dto.RecommendRequest;
import com.recommend.common.dto.RecommendResponse;
import com.recommend.common.utils.Result;
import com.recommend.service.StreamerSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 主播搜索控制器
 *
 * @author liujiandong
 */
@RestController
@RequestMapping("/api/streamer")
@Slf4j
public class StreamerSearchController {

    @Autowired
    private StreamerSearchService streamerSearchService;

    /**
     * 搜索主播列表
     */
    @PostMapping("/search/live")
    public Result<RecommendResponse> searchStreamerList(
            @RequestParam @NotNull Long userId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        
        log.info("搜索主播列表 - 用户ID: {}, 关键词: {}, 分类: {}, 地区: {}", 
                userId, keyword, category, region);
        
        RecommendRequest request = new RecommendRequest();
        request.setUserId(userId);
        request.setKeyword(keyword);
        request.setCategory(category);
        request.setRegion(region);
        request.setMinLevel(minLevel);
        request.setMaxLevel(maxLevel);
        request.setPage(page);
        request.setLimit(limit);
        
        RecommendResponse response = streamerSearchService.searchStreamers(request);
        return Result.success(response);
    }

    /**
     * 技能匹配搜索
     */
    @PostMapping("/search/skill-match")
    public Result<RecommendResponse> searchBySkillMatch(
            @RequestParam @NotNull Long userId,
            @RequestParam @NotNull String category,
            @RequestParam(required = false) Integer level,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        
        log.info("技能匹配搜索 - 用户ID: {}, 分类: {}, 等级: {}", userId, category, level);
        
        RecommendRequest request = new RecommendRequest();
        request.setUserId(userId);
        request.setCategory(category);
        request.setLevel(level);
        request.setPage(page);
        request.setLimit(limit);
        
        RecommendResponse response = streamerSearchService.skillMatchSearch(request);
        return Result.success(response);
    }

    /**
     * 新用户推荐
     */
    @PostMapping("/recommend/newbie")
    public Result<RecommendResponse> newbieRecommend(
            @RequestParam @NotNull Long userId,
            @RequestParam(required = false) String preferences,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        log.info("新用户推荐 - 用户ID: {}, 偏好: {}", userId, preferences);
        
        RecommendRequest request = new RecommendRequest();
        request.setUserId(userId);
        request.setPreferences(preferences);
        request.setPage(page);
        request.setLimit(limit);
        
        RecommendResponse response = streamerSearchService.newbieRecommend(request);
        return Result.success(response);
    }

    /**
     * 热门推荐
     */
    @PostMapping("/recommend/hot")
    public Result<RecommendResponse> hotRecommend(
            @RequestParam @NotNull Long userId,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        log.info("热门推荐 - 用户ID: {}, 分类: {}", userId, category);
        
        RecommendRequest request = new RecommendRequest();
        request.setUserId(userId);
        request.setCategory(category);
        request.setPage(page);
        request.setLimit(limit);
        
        RecommendResponse response = streamerSearchService.hotRecommend(request);
        return Result.success(response);
    }

    /**
     * 个性化推荐
     */
    @PostMapping("/recommend/personal")
    public Result<RecommendResponse> personalRecommend(
            @RequestParam @NotNull Long userId,
            @RequestParam(required = false) String scene,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        log.info("个性化推荐 - 用户ID: {}, 场景: {}", userId, scene);
        
        RecommendRequest request = new RecommendRequest();
        request.setUserId(userId);
        request.setScene(scene);
        request.setPage(page);
        request.setLimit(limit);
        
        RecommendResponse response = streamerSearchService.personalRecommend(request);
        return Result.success(response);
    }
} 