package com.recommend.monitor;

import com.recommend.algorithm.RecommendMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@Slf4j
public class RecommendMetricsCalculator {
    
    /**
     * 计算多样性指标
     * 使用推荐结果的标签分布来衡量
     */
    public double calculateDiversity(List<Map<String, Double>> recommendations) {
        if (recommendations.isEmpty()) {
            return 0.0;
        }
        
        // 计算所有标签的分布
        Map<String, Double> tagDistribution = new HashMap<>();
        int totalTags = 0;
        
        for (Map<String, Double> item : recommendations) {
            for (Map.Entry<String, Double> tag : item.entrySet()) {
                tagDistribution.merge(tag.getKey(), tag.getValue(), Double::sum);
                totalTags++;
            }
        }
        
        // 计算香农熵
        double entropy = 0.0;
        for (double count : tagDistribution.values()) {
            double probability = count / totalTags;
            entropy -= probability * Math.log(probability);
        }
        
        // 归一化熵值
        return entropy / Math.log(tagDistribution.size());
    }
    
    /**
     * 计算新颖性指标
     * 基于推荐项目的流行度
     */
    public double calculateNovelty(List<Long> itemIds, Map<Long, Integer> itemPopularity) {
        if (itemIds.isEmpty()) {
            return 0.0;
        }
        
        double novelty = 0.0;
        for (Long itemId : itemIds) {
            int popularity = itemPopularity.getOrDefault(itemId, 0);
            novelty += Math.log(1.0 / (1.0 + popularity));
        }
        
        return novelty / itemIds.size();
    }
    
    /**
     * 计算时效性指标
     * 基于推荐项目的时间衰减
     */
    public double calculateTimeliness(List<Long> itemIds, Map<Long, Long> itemTimestamps) {
        if (itemIds.isEmpty()) {
            return 0.0;
        }
        
        long currentTime = System.currentTimeMillis();
        double timeliness = 0.0;
        
        for (Long itemId : itemIds) {
            long timestamp = itemTimestamps.getOrDefault(itemId, currentTime);
            double timeDiff = (currentTime - timestamp) / (24.0 * 60 * 60 * 1000); // 转换为天
            timeliness += Math.exp(-timeDiff / 30.0); // 30天衰减因子
        }
        
        return timeliness / itemIds.size();
    }
    
    /**
     * 计算冷启动性能指标
     * 基于新用户/新项目的推荐效果
     */
    public double calculateColdStartPerformance(List<Long> userIds, List<Long> itemIds,
                                              Map<Long, Integer> userInteractions,
                                              Map<Long, Integer> itemInteractions) {
        if (userIds.isEmpty() || itemIds.isEmpty()) {
            return 0.0;
        }
        
        int coldStartUsers = 0;
        int coldStartItems = 0;
        int totalUsers = userIds.size();
        int totalItems = itemIds.size();
        
        for (Long userId : userIds) {
            if (userInteractions.getOrDefault(userId, 0) < 5) {
                coldStartUsers++;
            }
        }
        
        for (Long itemId : itemIds) {
            if (itemInteractions.getOrDefault(itemId, 0) < 5) {
                coldStartItems++;
            }
        }
        
        double userColdStartRatio = (double) coldStartUsers / totalUsers;
        double itemColdStartRatio = (double) coldStartItems / totalItems;
        
        return (userColdStartRatio + itemColdStartRatio) / 2.0;
    }
    
    /**
     * 计算个性化程度指标
     * 基于用户之间的推荐差异
     */
    public double calculatePersonalization(List<List<Long>> userRecommendations) {
        if (userRecommendations.isEmpty()) {
            return 0.0;
        }
        
        double totalSimilarity = 0.0;
        int comparisonCount = 0;
        
        for (int i = 0; i < userRecommendations.size(); i++) {
            for (int j = i + 1; j < userRecommendations.size(); j++) {
                double similarity = calculateJaccardSimilarity(
                    userRecommendations.get(i),
                    userRecommendations.get(j)
                );
                totalSimilarity += similarity;
                comparisonCount++;
            }
        }
        
        return 1.0 - (totalSimilarity / comparisonCount);
    }
    
    /**
     * 计算Jaccard相似度
     */
    private double calculateJaccardSimilarity(List<Long> list1, List<Long> list2) {
        Set<Long> set1 = new HashSet<>(list1);
        Set<Long> set2 = new HashSet<>(list2);
        
        Set<Long> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        Set<Long> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return (double) intersection.size() / union.size();
    }
    
    /**
     * 计算推荐系统的整体评分
     */
    public double calculateOverallScore(RecommendMetrics metrics) {
        // 权重配置
        double precisionWeight = 0.2;
        double recallWeight = 0.2;
        double diversityWeight = 0.15;
        double noveltyWeight = 0.15;
        double timelinessWeight = 0.15;
        double coldStartWeight = 0.15;
        
        return metrics.getPrecision() * precisionWeight +
               metrics.getRecall() * recallWeight +
               metrics.getDiversity() * diversityWeight +
               metrics.getNovelty() * noveltyWeight +
               metrics.getTimeliness() * timelinessWeight +
               metrics.getColdStartPerformance() * coldStartWeight;
    }
} 