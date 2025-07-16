package com.recommend.service.feature;

import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FeatureExtractor {

    /**
     * 提取排序特征
     */
    public RankingFeatures extractFeatures(GameMaster master, UserProfile userProfile) {
        return RankingFeatures.builder()
                .master(master)
                .relevanceScore(calculateRelevanceScore(master, userProfile))
                .tagSimilarity(calculateTagSimilarity(master, userProfile))
                .regionMatch(calculateRegionMatch(master, userProfile))
                .ageMatch(calculateAgeMatch(master, userProfile))
                .gameTypeMatch(calculateGameTypeMatch(master, userProfile))
                .rating(master.getScore() != null ? master.getScore().doubleValue() : 0.0)
                .orderCount(master.getOrderCount())
                .completionRate(calculateCompletionRate(master))
                .responseTime(calculateResponseTime(master))
                .diversityScore(calculateDiversityScore(master))
                .categoryCount(calculateCategoryCount(master))
                .noveltyScore(calculateNoveltyScore(master))
                .popularityScore(calculatePopularityScore(master))
                .viewCount(calculateViewCount(master))
                .likeCount(calculateLikeCount(master))
                .commentCount(calculateCommentCount(master))
                .priceScore(calculatePriceScore(master))
                .priceRatio(calculatePriceRatio(master))
                .isPriceReasonable(isPriceReasonable(master))
                .timeDecayScore(calculateTimeDecayScore(master))
                .isOnline(isOnline(master))
                .availabilityScore(calculateAvailabilityScore(master))
                .personalizedScore(calculatePersonalizedScore(master, userProfile))
                .historicalPreference(calculateHistoricalPreference(master, userProfile))
                .behaviorSimilarity(calculateBehaviorSimilarity(master, userProfile))
                .customFeatures(extractCustomFeatures(master, userProfile))
                .build();
    }

    private Double calculateRelevanceScore(GameMaster master, UserProfile userProfile) {
        double score = 0.0;
        
        // 基于用户偏好标签和陪玩师标签的匹配度
        if (userProfile.getTags() != null && master.getTags() != null) {
            List<String> userTags = Arrays.asList(userProfile.getTags().split(","));
            List<String> masterTags = Arrays.asList(master.getTags().split(","));
            
            long matchCount = userTags.stream()
                .filter(masterTags::contains)
                .count();
            
            if (!userTags.isEmpty()) {
                score += (double) matchCount / userTags.size() * 0.4;
            }
        }
        
        // 基于游戏类型匹配
        if (userProfile.getGameTypes() != null && master.getGameTypes() != null) {
            List<String> userGameTypes = Arrays.asList(userProfile.getGameTypes().split(","));
            List<String> masterGameTypes = Arrays.asList(master.getGameTypes().split(","));
            
            long gameTypeMatchCount = userGameTypes.stream()
                .filter(masterGameTypes::contains)
                .count();
            
            if (!userGameTypes.isEmpty()) {
                score += (double) gameTypeMatchCount / userGameTypes.size() * 0.3;
            }
        }
        
        // 基于评分权重
        if (master.getScore() != null) {
            score += master.getScore().doubleValue() / 5.0 * 0.3;
        }
        
        return Math.min(1.0, score);
    }

    private Double calculateTagSimilarity(GameMaster master, UserProfile userProfile) {
        if (userProfile.getTags() == null || master.getTags() == null) {
            return 0.0;
        }
        
        List<String> userTags = Arrays.asList(userProfile.getTags().split(","));
        List<String> masterTags = Arrays.asList(master.getTags().split(","));
        
        if (userTags.isEmpty() || masterTags.isEmpty()) {
            return 0.0;
        }
        
        // 计算Jaccard相似度
        long intersectionCount = userTags.stream()
            .filter(masterTags::contains)
            .count();
        
        int unionSize = userTags.size() + masterTags.size() - (int) intersectionCount;
        
        return unionSize > 0 ? (double) intersectionCount / unionSize : 0.0;
    }

    private Double calculateRegionMatch(GameMaster master, UserProfile userProfile) {
        if (master.getRegion() != null && userProfile.getRegion() != null) {
            return master.getRegion().equals(userProfile.getRegion()) ? 1.0 : 0.0;
        }
        return 0.5;
    }

    private Double calculateAgeMatch(GameMaster master, UserProfile userProfile) {
        if (master.getAge() != null && userProfile.getAge() != null) {
            int ageDiff = Math.abs(master.getAge() - userProfile.getAge());
            return Math.max(0.0, 1.0 - ageDiff / 20.0);
        }
        return 0.5;
    }

    private Double calculateGameTypeMatch(GameMaster master, UserProfile userProfile) {
        if (userProfile.getGameTypes() == null || master.getGameTypes() == null) {
            return 0.0;
        }
        
        List<String> userGameTypes = Arrays.asList(userProfile.getGameTypes().split(","));
        List<String> masterGameTypes = Arrays.asList(master.getGameTypes().split(","));
        
        if (userGameTypes.isEmpty() || masterGameTypes.isEmpty()) {
            return 0.0;
        }
        
        long matchCount = userGameTypes.stream()
            .filter(masterGameTypes::contains)
            .count();
        
        return (double) matchCount / userGameTypes.size();
    }

    private Double calculateCompletionRate(GameMaster master) {
        // 基于订单完成情况计算完成率
        if (master.getOrderCount() != null && master.getOrderCount() > 0) {
            // 假设有完成订单数字段，这里用订单数和评分来估算
            if (master.getScore() != null) {
                // 评分越高，完成率越高的假设
                return Math.min(1.0, master.getScore().doubleValue() / 5.0 * 1.1);
            }
            return 0.8; // 默认完成率
        }
        return 0.5; // 新用户默认完成率
    }

    private Double calculateResponseTime(GameMaster master) {
        // 基于评分和订单数估算响应时间得分（评分高、订单多的通常响应快）
        if (master.getScore() != null && master.getOrderCount() != null) {
            double scoreWeight = master.getScore().doubleValue() / 5.0;
            double orderWeight = Math.min(1.0, master.getOrderCount() / 100.0);
            return (scoreWeight * 0.6 + orderWeight * 0.4);
        }
        return 0.5;
    }

    private Double calculateDiversityScore(GameMaster master) {
        // 基于游戏类型数量计算多样性
        if (master.getGameTypes() != null && !master.getGameTypes().isEmpty()) {
            String[] gameTypes = master.getGameTypes().split(",");
            // 游戏类型越多，多样性越高
            return Math.min(1.0, gameTypes.length / 10.0);
        }
        return 0.3;
    }

    private Integer calculateCategoryCount(GameMaster master) {
        // 计算陪玩师涉及的游戏类别数量
        if (master.getGameTypes() != null && !master.getGameTypes().isEmpty()) {
            return master.getGameTypes().split(",").length;
        }
        return 1;
    }

    private Double calculateNoveltyScore(GameMaster master) {
        // 基于注册时间计算新颖度（新注册的陪玩师新颖度高）
        if (master.getCreateTime() != null) {
            long daysSinceCreation = (System.currentTimeMillis() - master.getCreateTime().getTime()) / (1000 * 60 * 60 * 24);
            // 30天内的为新用户，新颖度较高
            if (daysSinceCreation <= 30) {
                return Math.max(0.3, 1.0 - daysSinceCreation / 30.0);
            }
        }
        return 0.2;
    }

    private Double calculatePopularityScore(GameMaster master) {
        // 基于订单数和评分计算热门度
        double score = 0.0;
        if (master.getOrderCount() != null) {
            score += Math.min(0.6, master.getOrderCount() / 200.0);
        }
        if (master.getScore() != null) {
            score += master.getScore().doubleValue() / 5.0 * 0.4;
        }
        return score;
    }

    private Long calculateViewCount(GameMaster master) {
        // 基于订单数和评分估算观看数
        if (master.getOrderCount() != null && master.getScore() != null) {
            // 订单数越多，评分越高，观看数越多
            return (long) (master.getOrderCount() * 10 + master.getScore().doubleValue() * 200);
        }
        return master.getOrderCount() != null ? master.getOrderCount() * 8L : 100L;
    }

    private Long calculateLikeCount(GameMaster master) {
        // 基于评分和订单数估算点赞数
        if (master.getScore() != null && master.getOrderCount() != null) {
            return (long) (master.getScore().doubleValue() * 20 + master.getOrderCount() * 0.8);
        }
        return master.getOrderCount() != null ? master.getOrderCount() / 2L : 10L;
    }

    private Long calculateCommentCount(GameMaster master) {
        // 基于订单数估算评论数（通常评论数是订单数的一定比例）
        if (master.getOrderCount() != null) {
            return (long) (master.getOrderCount() * 0.3);
        }
        return 5L;
    }

    private Double calculatePriceScore(GameMaster master) {
        // 基于价格计算得分（价格适中得分高）
        if (master.getPrice() != null) {
            double price = master.getPrice().doubleValue();
            // 假设合理价格区间为50-200，在此区间内得分较高
            if (price >= 50 && price <= 200) {
                return 1.0 - Math.abs(price - 125) / 125.0; // 125为中位数
            } else if (price < 50) {
                return 0.3 + price / 50.0 * 0.4; // 价格过低可能质量不高
            } else {
                return Math.max(0.1, 1.0 - (price - 200) / 300.0); // 价格过高性价比低
            }
        }
        return 0.5;
    }

    private Double calculatePriceRatio(GameMaster master) {
        // 计算价格性价比（基于评分和价格）
        if (master.getPrice() != null && master.getScore() != null) {
            double priceValue = master.getPrice().doubleValue();
            double scoreValue = master.getScore().doubleValue();
            if (priceValue > 0) {
                // 评分/价格比，值越高性价比越好
                return Math.min(1.0, scoreValue / priceValue * 50); // 归一化到0-1
            }
        }
        return 0.5;
    }

    private Boolean isPriceReasonable(GameMaster master) {
        // 判断价格是否合理（基于评分和市场价格）
        if (master.getPrice() != null && master.getScore() != null) {
            double price = master.getPrice().doubleValue();
            double score = master.getScore().doubleValue();
            
            // 根据评分判断合理价格区间
            double expectedMinPrice = score * 20; // 评分4.0对应80元
            double expectedMaxPrice = score * 60; // 评分4.0对应240元
            
            return price >= expectedMinPrice && price <= expectedMaxPrice;
        }
        return true; // 默认认为合理
    }

    private Double calculateTimeDecayScore(GameMaster master) {
        // 基于最后活跃时间计算时间衰减得分
        if (master.getUpdateTime() != null) {
            long hoursSinceUpdate = (System.currentTimeMillis() - master.getUpdateTime().getTime()) / (1000 * 60 * 60);
            // 24小时内活跃度最高，之后逐渐衰减
            if (hoursSinceUpdate <= 24) {
                return 1.0;
            } else if (hoursSinceUpdate <= 168) { // 一周内
                return 1.0 - (hoursSinceUpdate - 24) / 144.0 * 0.5; // 衰减到0.5
            } else {
                return Math.max(0.1, 0.5 - (hoursSinceUpdate - 168) / 720.0 * 0.4); // 继续衰减
            }
        }
        return 0.5;
    }

    private Boolean isOnline(GameMaster master) {
        return master.getStatus() != null && master.getStatus() == 1;
    }

    private Double calculateAvailabilityScore(GameMaster master) {
        // 基于在线状态和时间衰减计算可用性得分
        double score = 0.0;
        
        // 在线状态权重
        if (isOnline(master)) {
            score += 0.6;
        } else {
            score += 0.2;
        }
        
        // 时间活跃度权重
        score += calculateTimeDecayScore(master) * 0.4;
        
        return Math.min(1.0, score);
    }

    private Double calculatePersonalizedScore(GameMaster master, UserProfile userProfile) {
        // 综合个性化得分计算
        double score = 0.0;
        
        // 标签相似度权重
        score += calculateTagSimilarity(master, userProfile) * 0.3;
        
        // 游戏类型匹配权重
        score += calculateGameTypeMatch(master, userProfile) * 0.3;
        
        // 地区匹配权重
        score += calculateRegionMatch(master, userProfile) * 0.2;
        
        // 年龄匹配权重
        score += calculateAgeMatch(master, userProfile) * 0.2;
        
        return Math.min(1.0, score);
    }

    private Double calculateHistoricalPreference(GameMaster master, UserProfile userProfile) {
        // 基于用户历史偏好计算得分
        double score = 0.0;
        
        // 基于用户偏好的游戏类型
        if (userProfile.getGameTypes() != null && master.getGameTypes() != null) {
            score += calculateGameTypeMatch(master, userProfile) * 0.4;
        }
        
        // 基于用户偏好的地区
        if (userProfile.getRegion() != null && master.getRegion() != null) {
            score += calculateRegionMatch(master, userProfile) * 0.3;
        }
        
        // 基于用户偏好的价格范围（假设用户有价格偏好）
        if (master.getPrice() != null) {
            // 假设用户偏好中等价格（100-150区间）
            double price = master.getPrice().doubleValue();
            if (price >= 100 && price <= 150) {
                score += 0.3;
            } else {
                score += Math.max(0.1, 0.3 - Math.abs(price - 125) / 125.0 * 0.2);
            }
        }
        
        return Math.min(1.0, score);
    }

    private Double calculateBehaviorSimilarity(GameMaster master, UserProfile userProfile) {
        // 基于用户行为模式计算相似度
        double score = 0.0;
        
        // 活跃时间相似度（假设用户和陪玩师都有活跃时间偏好）
        score += calculateTimeDecayScore(master) * 0.3;
        
        // 服务质量偏好（基于评分）
        if (master.getScore() != null) {
            // 假设用户偏好高评分的陪玩师
            score += master.getScore().doubleValue() / 5.0 * 0.4;
        }
        
        // 经验偏好（基于订单数）
        if (master.getOrderCount() != null) {
            // 适度的订单数表示有经验但不会太忙
            double orderScore = Math.min(1.0, master.getOrderCount() / 100.0);
            if (orderScore > 0.8) {
                orderScore = 1.0 - (orderScore - 0.8) * 0.5; // 订单过多可能服务质量下降
            }
            score += orderScore * 0.3;
        }
        
        return Math.min(1.0, score);
    }

    private Map<String, Double> extractCustomFeatures(GameMaster master, UserProfile userProfile) {
        Map<String, Double> features = new HashMap<>();
        
        try {
            // 1. 互动频率特征
            features.put("interaction_frequency", calculateInteractionFrequency(master, userProfile));
            
            // 2. 时间偏好匹配特征
            features.put("time_preference_match", calculateTimePreferenceMatch(master, userProfile));
            
            // 3. 社交活跃度特征
            features.put("social_activity", calculateSocialActivity(master));
            
            // 4. 服务稳定性特征
            features.put("service_stability", calculateServiceStability(master));
            
            // 5. 用户反馈质量特征
            features.put("feedback_quality", calculateFeedbackQuality(master));
            
            // 6. 技能专业度特征
            features.put("skill_expertise", calculateSkillExpertise(master));
            
            // 7. 地理位置便利性特征
            features.put("location_convenience", calculateLocationConvenience(master, userProfile));
            
            // 8. 价格竞争力特征
            features.put("price_competitiveness", calculatePriceCompetitiveness(master));
            
        } catch (Exception e) {
            log.error("提取自定义特征失败", e);
        }
        
        return features;
    }
    
    /**
     * 计算互动频率特征
     */
    private Double calculateInteractionFrequency(GameMaster master, UserProfile userProfile) {
        // 基于历史互动数据计算频率
        if (master.getOrderCount() != null && userProfile.getOrderCount() != null) {
            double masterFreq = master.getOrderCount() / 30.0; // 月均订单
            double userFreq = userProfile.getOrderCount() / 30.0; // 用户月均订单
            return Math.min(1.0, Math.abs(masterFreq - userFreq) < 5 ? 1.0 : 0.5);
        }
        return 0.5;
    }
    
    /**
     * 计算时间偏好匹配特征
     */
    private Double calculateTimePreferenceMatch(GameMaster master, UserProfile userProfile) {
        // 基于活跃时间段匹配度
        if (master.getUpdateTime() != null && userProfile.getUpdateTime() != null) {
            long masterHour = (master.getUpdateTime().getTime() / (1000 * 60 * 60)) % 24;
            long userHour = (userProfile.getUpdateTime().getTime() / (1000 * 60 * 60)) % 24;
            long timeDiff = Math.abs(masterHour - userHour);
            return Math.max(0.0, 1.0 - timeDiff / 12.0); // 12小时内匹配度较高
        }
        return 0.5;
    }
    
    /**
     * 计算社交活跃度特征
     */
    private Double calculateSocialActivity(GameMaster master) {
        // 基于评论数、点赞数等社交指标
        long comments = calculateCommentCount(master);
        long likes = calculateLikeCount(master);
        double activity = (comments * 0.6 + likes * 0.4) / 1000.0;
        return Math.min(1.0, activity);
    }
    
    /**
     * 计算服务稳定性特征
     */
    private Double calculateServiceStability(GameMaster master) {
        // 基于完成率和响应时间的稳定性
        double completionRate = calculateCompletionRate(master);
        double responseScore = calculateResponseTime(master);
        return (completionRate * 0.7 + responseScore * 0.3);
    }
    
    /**
     * 计算用户反馈质量特征
     */
    private Double calculateFeedbackQuality(GameMaster master) {
        // 基于评分分布和评论质量
        if (master.getScore() != null) {
            double score = master.getScore().doubleValue();
            // 评分在4.5以上认为反馈质量高
            return score >= 4.5 ? 1.0 : score / 5.0;
        }
        return 0.5;
    }
    
    /**
     * 计算技能专业度特征
     */
    private Double calculateSkillExpertise(GameMaster master) {
        // 基于游戏类型数量和订单完成情况
        int categoryCount = calculateCategoryCount(master);
        double completionRate = calculateCompletionRate(master);
        
        // 专业度 = 技能广度 * 完成质量
        double breadth = Math.min(1.0, categoryCount / 5.0);
        return breadth * 0.4 + completionRate * 0.6;
    }
    
    /**
     * 计算地理位置便利性特征
     */
    private Double calculateLocationConvenience(GameMaster master, UserProfile userProfile) {
        // 基于地区匹配和时区考虑
        double regionMatch = calculateRegionMatch(master, userProfile);
        // 同地区便利性更高
        return regionMatch > 0.8 ? 1.0 : 0.6;
    }
    
    /**
     * 计算价格竞争力特征
     */
    private Double calculatePriceCompetitiveness(GameMaster master) {
        // 基于价格合理性和性价比
        double priceScore = calculatePriceScore(master);
        double priceRatio = calculatePriceRatio(master);
        return (priceScore * 0.5 + priceRatio * 0.5);
    }
} 