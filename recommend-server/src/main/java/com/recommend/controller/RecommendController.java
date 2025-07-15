package com.recommend.controller;

import com.recommend.common.dto.RecommendRequest;
import com.recommend.common.dto.RecommendResponse;
import com.recommend.common.exception.BusinessException;
import com.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RecommendController {
    
    private final RecommendService recommendService;
    
    /**
     * 获取游戏主推荐
     */
    @PostMapping("/masters")
    public ResponseEntity<RecommendResponse> recommendGameMasters(
        @Valid @RequestBody RecommendRequest request
    ) {
        try {
            log.info("Recommending game masters for user: {}", request.getUserId());
            RecommendResponse response = recommendService.recommendGameMasters(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error recommending game masters", e);
            throw BusinessException.of("RECOMMEND_ERROR", "推荐游戏主失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取游戏推荐
     */
    @PostMapping("/games")
    public ResponseEntity<RecommendResponse> recommendGames(
        @Valid @RequestBody RecommendRequest request
    ) {
        try {
            log.info("Recommending games for user: {}", request.getUserId());
            RecommendResponse response = recommendService.recommendGames(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error recommending games", e);
            throw BusinessException.of("RECOMMEND_ERROR", "推荐游戏失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取个性化推荐（混合推荐）
     */
    @PostMapping("/personalized")
    public ResponseEntity<RecommendResponse> getPersonalizedRecommendations(
        @Valid @RequestBody RecommendRequest request
    ) {
        try {
            log.info("Getting personalized recommendations for user: {}", request.getUserId());
            request.setAlgorithm("hybrid");
            RecommendResponse response = recommendService.getPersonalizedRecommendations(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting personalized recommendations", e);
            throw BusinessException.of("RECOMMEND_ERROR", "获取个性化推荐失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取热门推荐
     */
    @GetMapping("/hot")
    public ResponseEntity<RecommendResponse> getHotRecommendations(
        @RequestParam Long userId,
        @RequestParam(defaultValue = "10") Integer count
    ) {
        try {
            log.info("Getting hot recommendations for user: {}", userId);
            RecommendResponse response = recommendService.getHotRecommendations(userId, count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting hot recommendations", e);
            throw BusinessException.of("RECOMMEND_ERROR", "获取热门推荐失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取新人推荐（冷启动）
     */
    @GetMapping("/newcomer")
    public ResponseEntity<RecommendResponse> getNewcomerRecommendations(
        @RequestParam Long userId,
        @RequestParam(defaultValue = "10") Integer count
    ) {
        try {
            log.info("Getting newcomer recommendations for user: {}", userId);
            RecommendResponse response = recommendService.getNewcomerRecommendations(userId, count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting newcomer recommendations", e);
            throw BusinessException.of("RECOMMEND_ERROR", "获取新人推荐失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取相似用户推荐
     */
    @GetMapping("/similar-users")
    public ResponseEntity<RecommendResponse> getSimilarUserRecommendations(
        @RequestParam Long userId,
        @RequestParam(defaultValue = "10") Integer count
    ) {
        try {
            log.info("Getting similar user recommendations for user: {}", userId);
            RecommendResponse response = recommendService.getSimilarUserRecommendations(userId, count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting similar user recommendations", e);
            throw BusinessException.of("RECOMMEND_ERROR", "获取相似用户推荐失败: " + e.getMessage());
        }
    }
    
    /**
     * 记录用户行为
     */
    @PostMapping("/behavior")
    public ResponseEntity<Void> recordUserBehavior(
        @RequestParam Long userId,
        @RequestParam Integer type,
        @RequestParam Long targetId,
        @RequestParam Integer targetType,
        @RequestParam(required = false) String content
    ) {
        try {
            log.info("Recording user behavior: userId={}, type={}, targetId={}", userId, type, targetId);
            recommendService.recordUserBehavior(userId, type, targetId, targetType, content);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error recording user behavior", e);
            throw BusinessException.of("BEHAVIOR_ERROR", "记录用户行为失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新用户偏好
     */
    @PostMapping("/preferences")
    public ResponseEntity<Void> updateUserPreferences(
        @RequestParam Long userId,
        @RequestParam List<String> tags,
        @RequestParam(required = false) String region,
        @RequestParam(required = false) String gender
    ) {
        try {
            log.info("Updating user preferences for user: {}", userId);
            recommendService.updateUserPreferences(userId, tags, region, gender);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error updating user preferences", e);
            throw BusinessException.of("PREFERENCE_ERROR", "更新用户偏好失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取推荐解释
     */
    @GetMapping("/explanation")
    public ResponseEntity<String> getRecommendationExplanation(
        @RequestParam Long userId,
        @RequestParam Long masterId
    ) {
        try {
            log.info("Getting recommendation explanation for user: {} and master: {}", userId, masterId);
            String explanation = recommendService.getRecommendationExplanation(userId, masterId);
            return ResponseEntity.ok(explanation);
        } catch (Exception e) {
            log.error("Error getting recommendation explanation", e);
            throw BusinessException.of("EXPLANATION_ERROR", "获取推荐解释失败: " + e.getMessage());
        }
    }
} 