package com.recommend.service.feature;

import com.recommend.common.entity.GameMaster;
import lombok.Builder;
import lombok.Data;
import java.util.Map;
import java.util.HashMap;

/**
 * 排序特征类
 * 包含所有用于排序的特征值
 */
@Data
@Builder
public class RankingFeatures {
    
    // 基础信息
    private GameMaster master;
    
    // 相关性特征
    private Double relevanceScore;
    private Double tagSimilarity;
    private Double regionMatch;
    private Double ageMatch;
    private Double gameTypeMatch;
    
    // 质量特征
    private Double rating;
    private Integer orderCount;
    private Double completionRate;
    private Double responseTime;
    
    // 多样性特征
    private Double diversityScore;
    private Integer categoryCount;
    private Double noveltyScore;
    
    // 热度特征
    private Double popularityScore;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    
    // 价格特征
    private Double priceScore;
    private Double priceRatio;
    private Boolean isPriceReasonable;
    
    // 时间特征
    private Double timeDecayScore;
    private Boolean isOnline;
    private Double availabilityScore;

    private Double score;

    // 个性化特征
    private Double personalizedScore;
    private Double historicalPreference;
    private Double behaviorSimilarity;
    
    // 自定义特征
    private Map<String, Double> customFeatures;
    
    /**
     * 计算综合得分
     */
    public Double calculateTotalScore() {
        return calculateTotalScore(getDefaultWeights());
    }
    
    /**
     * 根据权重计算综合得分
     */
    public Double calculateTotalScore(Map<String, Double> weights) {
        double totalScore = 0.0;
        
        // 相关性得分 (30%)
        totalScore += (relevanceScore != null ? relevanceScore : 0.0) * weights.getOrDefault("relevance", 0.3);
        
        // 质量得分 (25%)
        double qualityScore = calculateQualityScore();
        totalScore += qualityScore * weights.getOrDefault("quality", 0.25);
        
        // 热度得分 (20%)
        double popularityScore = calculatePopularityScore();
        totalScore += popularityScore * weights.getOrDefault("popularity", 0.2);
        
        // 价格得分 (15%)
        totalScore += (priceScore != null ? priceScore : 0.0) * weights.getOrDefault("price", 0.15);
        
        // 时间得分 (10%)
        totalScore += (timeDecayScore != null ? timeDecayScore : 0.0) * weights.getOrDefault("time", 0.1);
        
        return totalScore;
    }
    
    /**
     * 计算质量得分
     */
    private Double calculateQualityScore() {
        double qualityScore = 0.0;
        int count = 0;
        
        if (rating != null) {
            qualityScore += rating;
            count++;
        }
        
        if (completionRate != null) {
            qualityScore += completionRate;
            count++;
        }
        
        if (responseTime != null) {
            // 响应时间越短得分越高
            qualityScore += Math.max(0, 1.0 - responseTime / 100.0);
            count++;
        }
        
        return count > 0 ? qualityScore / count : 0.0;
    }
    
    /**
     * 计算热度得分
     */
    private Double calculatePopularityScore() {
        double popularityScore = 0.0;
        int count = 0;
        
        if (viewCount != null) {
            popularityScore += Math.min(1.0, viewCount / 10000.0);
            count++;
        }
        
        if (likeCount != null) {
            popularityScore += Math.min(1.0, likeCount / 1000.0);
            count++;
        }
        
        if (commentCount != null) {
            popularityScore += Math.min(1.0, commentCount / 100.0);
            count++;
        }
        
        return count > 0 ? popularityScore / count : 0.0;
    }
    
    /**
     * 获取默认权重
     */
    private Map<String, Double> getDefaultWeights() {
        Map<String, Double> weights = new HashMap<>();
        weights.put("relevance", 0.3);
        weights.put("quality", 0.25);
        weights.put("popularity", 0.2);
        weights.put("price", 0.15);
        weights.put("time", 0.1);
        return weights;
    }
} 