package com.recommend.service.feature;

import com.recommend.common.entity.GameMaster;
import lombok.Data;
import lombok.Builder;
import java.util.Map;

@Data
@Builder
public class RankingFeatures {
    
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
    
    // 热门度特征
    private Double popularityScore;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    
    // 价格特征
    private Double priceScore;
    private Double priceRatio;
    private Boolean isPriceReasonable;
    
    // 时间特征
    private Double timeDecayScore;
    private Boolean isOnline;
    private Double availabilityScore;
    
    // 个性化特征
    private Double personalizedScore;
    private Double historicalPreference;
    private Double behaviorSimilarity;
    
    // 其他特征
    private Map<String, Double> customFeatures;
    
    /**
     * 计算综合得分
     */
    public Double calculateTotalScore() {
        return relevanceScore * 0.3 + 
               rating * 0.2 + 
               popularityScore * 0.2 + 
               diversityScore * 0.15 + 
               personalizedScore * 0.15;
    }
} 