package com.recommend.monitor;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class RecommendMetricsSnapshot {
    
    private String id;
    private LocalDateTime timestamp;
    private String type;
    
    // 推荐系统核心指标
    private Long totalRecommendations;
    private Double averageResponseTime;
    private Double errorRate;
    private Double cacheHitRate;
    
    // 推荐质量指标
    private Double clickThroughRate;
    private Double conversionRate;
    private Double diversityScore;
    private Double noveltyScore;
    private Double coverageScore;
    
    // 用户行为指标
    private Long activeUsers;
    private Long newUsers;
    private Double userSatisfactionScore;
    private Double userEngagementScore;
    
    // 陪玩师指标
    private Long activeMasters;
    private Long newMasters;
    private Double masterUtilizationRate;
    private Double masterSatisfactionScore;
    
    // 系统性能指标
    private Double cpuUsage;
    private Double memoryUsage;
    private Double diskUsage;
    private Double networkUsage;
    
    // 业务指标
    private Long totalOrders;
    private Double totalRevenue;
    private Double averageOrderValue;
    private Double orderCompletionRate;
    
    // 算法指标
    private Double algorithmAccuracy;
    private Double algorithmPrecision;
    private Double algorithmRecall;
    private Double algorithmF1Score;
    
    // 自定义指标
    private Map<String, Double> customMetrics;
    
    /**
     * 获取失败率
     */
    public Double getFailureRate() {
        return errorRate;
    }
    
    /**
     * 获取平均精确率
     */
    public Double getAveragePrecision() {
        return clickThroughRate * 0.8; // 简化计算
    }
    
    /**
     * 获取平均召回率
     */
    public Double getAverageRecall() {
        return conversionRate * 0.9; // 简化计算
    }
    
    /**
     * 获取多样性得分
     */
    public Double getDiversity() {
        return diversityScore;
    }
    
    /**
     * 生成报告
     */
    public String generateReport() {
        return String.format(
            "推荐系统监控报告 [%s]\n" +
            "总推荐数: %d\n" +
            "平均响应时间: %.2fms\n" +
            "错误率: %.2f%%\n" +
            "缓存命中率: %.2f%%\n" +
            "点击率: %.2f%%\n" +
            "转化率: %.2f%%\n",
            timestamp,
            totalRecommendations,
            averageResponseTime,
            errorRate * 100,
            cacheHitRate * 100,
            clickThroughRate * 100,
            conversionRate * 100
        );
    }
    
    // 告警信息
    private Integer alertCount;
    private Integer criticalAlertCount;
    private Integer warningAlertCount;
    
    // 数据质量指标
    private Double dataCompleteness;
    private Double dataAccuracy;
    private Double dataFreshness;
    
    /**
     * 计算健康评分
     */
    public Double calculateHealthScore() {
        double score = 100.0;
        
        // 根据错误率扣分
        if (errorRate != null && errorRate > 0.01) {
            score -= errorRate * 1000;
        }
        
        // 根据响应时间扣分
        if (averageResponseTime != null && averageResponseTime > 1000) {
            score -= (averageResponseTime - 1000) / 100;
        }
        
        // 根据缓存命中率加分
        if (cacheHitRate != null && cacheHitRate > 0.8) {
            score += (cacheHitRate - 0.8) * 50;
        }
        
        // 根据告警数量扣分
        if (criticalAlertCount != null && criticalAlertCount > 0) {
            score -= criticalAlertCount * 10;
        }
        
        return Math.max(0.0, Math.min(100.0, score));
    }
} 