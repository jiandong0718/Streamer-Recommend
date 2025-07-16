package com.recommend.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class RecommendMetricsCollector {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private final AtomicLong totalRecommendations = new AtomicLong(0);
    private final AtomicLong successfulRecommendations = new AtomicLong(0);
    private final AtomicLong failedRecommendations = new AtomicLong(0);
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);
    private final AtomicReference<Double> averageResponseTime = new AtomicReference<>(0.0);
    
    private final Map<String, AtomicLong> userBehaviorCounts = new HashMap<>();
    private final Map<String, AtomicLong> algorithmUsageCounts = new HashMap<>();
    
    /**
     * 记录推荐请求
     */
    public void recordRecommendation(boolean success, long responseTime) {
        totalRecommendations.incrementAndGet();
        if (success) {
            successfulRecommendations.incrementAndGet();
        } else {
            failedRecommendations.incrementAndGet();
        }
        updateAverageResponseTime(responseTime);
    }
    
    /**
     * 记录缓存命中
     */
    public void recordCacheHit() {
        cacheHits.incrementAndGet();
    }
    
    /**
     * 记录缓存未命中
     */
    public void recordCacheMiss() {
        cacheMisses.incrementAndGet();
    }
    
    /**
     * 记录用户行为
     */
    public void recordUserBehavior(String behavior) {
        userBehaviorCounts.computeIfAbsent(behavior, k -> new AtomicLong(0)).incrementAndGet();
    }
    
    /**
     * 记录算法使用
     */
    public void recordAlgorithmUsage(String algorithm) {
        algorithmUsageCounts.computeIfAbsent(algorithm, k -> new AtomicLong(0)).incrementAndGet();
    }
    
    /**
     * 获取指标快照
     */
    public RecommendMetricsSnapshot getMetricsSnapshot() {
        return RecommendMetricsSnapshot.builder()
            .id(UUID.randomUUID().toString())
            .timestamp(LocalDateTime.now())
            .type("snapshot")
            .totalRecommendations(totalRecommendations.get())
            .averageResponseTime(averageResponseTime.get())
            .errorRate(calculateErrorRate())
            .cacheHitRate(calculateCacheHitRate())
            .clickThroughRate(calculateClickThroughRate())
            .conversionRate(calculateConversionRate())
            .diversityScore(calculateDiversityScore())
            .noveltyScore(calculateNoveltyScore())
            .coverageScore(calculateCoverageScore())
            .activeUsers(calculateActiveUsers())
            .newUsers(calculateNewUsers())
            .userSatisfactionScore(calculateUserSatisfactionScore())
            .userEngagementScore(calculateUserEngagementScore())
            .activeMasters(calculateActiveMasters())
            .newMasters(calculateNewMasters())
            .masterUtilizationRate(calculateMasterUtilizationRate())
            .masterSatisfactionScore(calculateMasterSatisfactionScore())
            .cpuUsage(getCpuUsage())
            .memoryUsage(getMemoryUsage())
            .diskUsage(getDiskUsage())
            .networkUsage(getNetworkUsage())
            .totalOrders(calculateTotalOrders())
            .totalRevenue(calculateTotalRevenue())
            .averageOrderValue(calculateAverageOrderValue())
            .orderCompletionRate(calculateOrderCompletionRate())
            .algorithmAccuracy(calculateAlgorithmAccuracy())
            .algorithmPrecision(calculateAlgorithmPrecision())
            .algorithmRecall(calculateAlgorithmRecall())
            .algorithmF1Score(calculateAlgorithmF1Score())
            .customMetrics(getCustomMetrics())
            .alertCount(getAlertCount())
            .criticalAlertCount(getCriticalAlertCount())
            .warningAlertCount(getWarningAlertCount())
            .dataCompleteness(calculateDataCompleteness())
            .dataAccuracy(calculateDataAccuracy())
            .dataFreshness(calculateDataFreshness())
            .build();
    }
    
    /**
     * 重置指标
     */
    public void resetMetrics() {
        totalRecommendations.set(0);
        successfulRecommendations.set(0);
        failedRecommendations.set(0);
        cacheHits.set(0);
        cacheMisses.set(0);
        averageResponseTime.set(0.0);
        userBehaviorCounts.clear();
        algorithmUsageCounts.clear();
    }
    
    private void updateAverageResponseTime(long responseTime) {
        // 简单的移动平均算法
        double current = averageResponseTime.get();
        double newAverage = (current * 0.9) + (responseTime * 0.1);
        averageResponseTime.set(newAverage);
    }
    
    private Double calculateErrorRate() {
        long total = totalRecommendations.get();
        return total > 0 ? (double) failedRecommendations.get() / total : 0.0;
    }
    
    private Double calculateCacheHitRate() {
        long totalCache = cacheHits.get() + cacheMisses.get();
        return totalCache > 0 ? (double) cacheHits.get() / totalCache : 0.0;
    }
    
    // 以下方法需要根据实际业务逻辑实现
    private Double calculateClickThroughRate() {
        try {
            // 从Redis或数据库获取点击和展示数据
            String clickKey = "recommend:metrics:clicks:" + getCurrentHour();
            String showKey = "recommend:metrics:shows:" + getCurrentHour();
            
            String clicksStr = redisTemplate.opsForValue().get(clickKey);
            String showsStr = redisTemplate.opsForValue().get(showKey);
            
            long clicks = clicksStr != null ? Long.parseLong(clicksStr) : 0;
            long shows = showsStr != null ? Long.parseLong(showsStr) : 0;
            
            return shows > 0 ? (double) clicks / shows : 0.0;
        } catch (Exception e) {
            log.error("计算点击率失败", e);
            return 0.05; // 默认值
        }
    }
    
    private Double calculateConversionRate() {
        try {
            // 从Redis或数据库获取转化和点击数据
            String conversionKey = "recommend:metrics:conversions:" + getCurrentHour();
            String clickKey = "recommend:metrics:clicks:" + getCurrentHour();
            
            String conversionsStr = redisTemplate.opsForValue().get(conversionKey);
            String clicksStr = redisTemplate.opsForValue().get(clickKey);
            
            long conversions = conversionsStr != null ? Long.parseLong(conversionsStr) : 0;
            long clicks = clicksStr != null ? Long.parseLong(clicksStr) : 0;
            
            return clicks > 0 ? (double) conversions / clicks : 0.0;
        } catch (Exception e) {
            log.error("计算转化率失败", e);
            return 0.02; // 默认值
        }
    }
    
    private Double calculateDiversityScore() {
        try {
            // 计算推荐结果的多样性
            // 这里简化为基于推荐的陪玩师类型多样性
            String diversityKey = "recommend:metrics:diversity:" + getCurrentHour();
            String diversityStr = redisTemplate.opsForValue().get(diversityKey);
            
            if (diversityStr != null) {
                return Double.parseDouble(diversityStr);
            }
            
            // 如果没有缓存数据，计算当前的多样性
            return calculateCurrentDiversityScore();
        } catch (Exception e) {
            log.error("计算多样性得分失败", e);
            return 0.7; // 默认值
        }
    }
    
    private Double calculateNoveltyScore() {
        try {
            // 计算推荐结果的新颖性
            String noveltyKey = "recommend:metrics:novelty:" + getCurrentHour();
            String noveltyStr = redisTemplate.opsForValue().get(noveltyKey);
            
            if (noveltyStr != null) {
                return Double.parseDouble(noveltyStr);
            }
            
            // 计算新陪玩师在推荐中的比例
            return calculateCurrentNoveltyScore();
        } catch (Exception e) {
            log.error("计算新颖性得分失败", e);
            return 0.6; // 默认值
        }
    }
    
    private Double calculateCoverageScore() {
        try {
            // 计算推荐覆盖率（被推荐的陪玩师占总陪玩师的比例）
            String coverageKey = "recommend:metrics:coverage:" + getCurrentHour();
            String coverageStr = redisTemplate.opsForValue().get(coverageKey);
            
            if (coverageStr != null) {
                return Double.parseDouble(coverageStr);
            }
            
            return calculateCurrentCoverageScore();
        } catch (Exception e) {
            log.error("计算覆盖率得分失败", e);
            return 0.8; // 默认值
        }
    }
    
    /**
     * 获取当前小时标识
     */
    private String getCurrentHour() {
        return String.valueOf(System.currentTimeMillis() / (1000 * 60 * 60));
    }
    
    /**
     * 计算当前多样性得分
     */
    private Double calculateCurrentDiversityScore() {
        // 简化实现：基于推荐的游戏类型数量
        try {
            Set<String> gameTypes = new HashSet<>();
            // 这里应该从推荐日志中获取数据
            // 暂时返回模拟值
            return 0.7;
        } catch (Exception e) {
            return 0.7;
        }
    }
    
    /**
     * 计算当前新颖性得分
     */
    private Double calculateCurrentNoveltyScore() {
        // 简化实现：基于新注册陪玩师的推荐比例
        try {
            // 这里应该查询最近推荐的陪玩师中新用户的比例
            return 0.6;
        } catch (Exception e) {
            return 0.6;
        }
    }
    
    /**
     * 计算当前覆盖率得分
     */
    private Double calculateCurrentCoverageScore() {
        // 简化实现：被推荐陪玩师数量 / 总陪玩师数量
        try {
            // 这里应该查询实际的覆盖率数据
            return 0.8;
        } catch (Exception e) {
            return 0.8;
        }
    }
    
    private Long calculateActiveUsers() {
        // TODO: 实现活跃用户数计算
        return 1000L;
    }
    
    private Long calculateNewUsers() {
        // TODO: 实现新用户数计算
        return 50L;
    }
    
    private Double calculateUserSatisfactionScore() {
        // TODO: 实现用户满意度计算
        return 4.2;
    }
    
    private Double calculateUserEngagementScore() {
        // TODO: 实现用户参与度计算
        return 0.65;
    }
    
    private Long calculateActiveMasters() {
        // TODO: 实现活跃陪玩师数计算
        return 200L;
    }
    
    private Long calculateNewMasters() {
        // TODO: 实现新陪玩师数计算
        return 10L;
    }
    
    private Double calculateMasterUtilizationRate() {
        // TODO: 实现陪玩师利用率计算
        return 0.75;
    }
    
    private Double calculateMasterSatisfactionScore() {
        // TODO: 实现陪玩师满意度计算
        return 4.0;
    }
    
    private Double getCpuUsage() {
        // TODO: 实现CPU使用率获取
        return 0.45;
    }
    
    private Double getMemoryUsage() {
        // TODO: 实现内存使用率获取
        return 0.60;
    }
    
    private Double getDiskUsage() {
        // TODO: 实现磁盘使用率获取
        return 0.30;
    }
    
    private Double getNetworkUsage() {
        // TODO: 实现网络使用率获取
        return 0.25;
    }
    
    private Long calculateTotalOrders() {
        // TODO: 实现总订单数计算
        return 5000L;
    }
    
    private Double calculateTotalRevenue() {
        // TODO: 实现总收入计算
        return 50000.0;
    }
    
    private Double calculateAverageOrderValue() {
        // TODO: 实现平均订单价值计算
        return 100.0;
    }
    
    private Double calculateOrderCompletionRate() {
        // TODO: 实现订单完成率计算
        return 0.95;
    }
    
    private Double calculateAlgorithmAccuracy() {
        // TODO: 实现算法准确率计算
        return 0.85;
    }
    
    private Double calculateAlgorithmPrecision() {
        // TODO: 实现算法精确率计算
        return 0.80;
    }
    
    private Double calculateAlgorithmRecall() {
        // TODO: 实现算法召回率计算
        return 0.75;
    }
    
    private Double calculateAlgorithmF1Score() {
        // TODO: 实现算法F1得分计算
        return 0.77;
    }
    
    private Map<String, Double> getCustomMetrics() {
        Map<String, Double> metrics = new HashMap<>();
        // TODO: 实现自定义指标获取
        return metrics;
    }
    
    private Integer getAlertCount() {
        // TODO: 实现告警数量获取
        return 5;
    }
    
    private Integer getCriticalAlertCount() {
        // TODO: 实现严重告警数量获取
        return 1;
    }
    
    private Integer getWarningAlertCount() {
        // TODO: 实现警告数量获取
        return 4;
    }
    
    private Double calculateDataCompleteness() {
        // TODO: 实现数据完整性计算
        return 0.95;
    }
    
    private Double calculateDataAccuracy() {
        // TODO: 实现数据准确性计算
        return 0.98;
    }
    
    private Double calculateDataFreshness() {
        // TODO: 实现数据新鲜度计算
        return 0.90;
    }
} 