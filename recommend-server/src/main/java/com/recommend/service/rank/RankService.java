package com.recommend.service.rank;

import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.UserProfile;
import com.recommend.service.UserProfileService;
import com.recommend.service.feature.FeatureExtractor;
import com.recommend.service.feature.RankingFeatures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RankService {
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Autowired
    private FeatureExtractor featureExtractor;
    
    public List<GameMaster> rankCandidates(List<GameMaster> candidates, Long userId) {
        // 1. 获取用户画像
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        
        // 2. 特征提取
        List<RankingFeatures> features = candidates.stream()
            .map(master -> featureExtractor.extractFeatures(master, userProfile))
            .collect(Collectors.toList());
        
        // 3. 计算得分
        List<RankedItem> rankedItems = calculateScores(features);
        
        // 4. 排序
        return rankedItems.stream()
            .sorted(Comparator.comparing(RankedItem::getScore).reversed())
            .map(RankedItem::getMaster)
            .collect(Collectors.toList());
    }
    
    private List<RankedItem> calculateScores(List<RankingFeatures> features) {
        // 实现基于特征的得分计算逻辑
        return features.stream()
            .map(feature -> {
                double score = calculateScore(feature);
                return new RankedItem(feature.getMaster(), score);
            })
            .collect(Collectors.toList());
    }
    
    private double calculateScore(RankingFeatures features) {
        // 实现具体的得分计算逻辑
        double relevanceScore = calculateRelevanceScore(features);
        double diversityScore = calculateDiversityScore(features);
        double popularityScore = calculatePopularityScore(features);
        
        return 0.4 * relevanceScore + 0.3 * diversityScore + 0.3 * popularityScore;
    }
    
    private double calculateRelevanceScore(RankingFeatures features) {
        double score = 0.0;
        
        // 标签匹配度权重
        if (features.getTagSimilarity() != null) {
            score += features.getTagSimilarity() * 0.4;
        }
        
        // 游戏类型匹配度权重
        if (features.getGameTypeMatch() != null) {
            score += features.getGameTypeMatch() * 0.3;
        }
        
        // 地区匹配度权重
        if (features.getRegionMatch() != null) {
            score += features.getRegionMatch() * 0.2;
        }
        
        // 年龄匹配度权重
        if (features.getAgeMatch() != null) {
            score += features.getAgeMatch() * 0.1;
        }
        
        return Math.min(1.0, score);
    }
    
    private double calculateDiversityScore(RankingFeatures features) {
        double score = 0.0;
        
        // 游戏类型多样性
        if (features.getCategoryCount() != null) {
            score += Math.min(1.0, features.getCategoryCount() / 5.0) * 0.5;
        }
        
        // 技能多样性（基于标签数量）
        if (features.getDiversityScore() != null) {
            score += features.getDiversityScore() * 0.3;
        }
        
        // 新颖性得分
        if (features.getNoveltyScore() != null) {
            score += features.getNoveltyScore() * 0.2;
        }
        
        return Math.min(1.0, score);
    }
    
    private double calculatePopularityScore(RankingFeatures features) {
        double score = 0.0;
        
        // 评分权重
        if (features.getScore() != null) {
            score += features.getScore() / 5.0 * 0.4;
        }
        
        // 订单数权重（归一化处理）
        if (features.getOrderCount() != null) {
            score += Math.min(1.0, features.getOrderCount() / 200.0) * 0.3;
        }
        
        // 观看数权重
        if (features.getViewCount() != null) {
            score += Math.min(1.0, features.getViewCount() / 10000.0) * 0.2;
        }
        
        // 点赞数权重
        if (features.getLikeCount() != null) {
            score += Math.min(1.0, features.getLikeCount() / 1000.0) * 0.1;
        }
        
        return Math.min(1.0, score);
    }
} 