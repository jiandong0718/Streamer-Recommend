package com.recommend.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推荐算法评估指标
 * 包含推荐系统的各种评估指标
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendMetrics {
    
    /**
     * 准确率 (Precision)
     * 推荐结果中相关项目的比例
     */
    private Double precision;
    
    /**
     * 召回率 (Recall)
     * 相关项目中被推荐的比例
     */
    private Double recall;
    
    /**
     * F1分数
     * 准确率和召回率的调和平均数
     */
    private Double f1Score;
    
    /**
     * 平均倒数排名 (Mean Reciprocal Rank)
     * 第一个相关结果的平均倒数排名
     */
    private Double mrr;
    
    /**
     * 归一化折损累积增益 (Normalized Discounted Cumulative Gain)
     * 考虑排序位置的评估指标
     */
    private Double ndcg;
    
    /**
     * 覆盖率 (Coverage)
     * 被推荐的项目占总项目的比例
     */
    private Double coverage;
    
    /**
     * 多样性 (Diversity)
     * 推荐结果的多样性程度
     */
    private Double diversity;
    
    /**
     * 新颖性 (Novelty)
     * 推荐结果的新颖程度
     */
    private Double novelty;
    
    /**
     * 时效性 (Timeliness)
     * 推荐结果的时效性
     */
    private Double timeliness;
    
    /**
     * 冷启动性能 (Cold Start Performance)
     * 对新用户的推荐效果
     */
    private Double coldStartPerformance;
    
    /**
     * 平均响应时间 (毫秒)
     */
    private Double averageResponseTime;
    
    /**
     * 缓存命中率
     */
    private Double cacheHitRate;
    
    /**
     * 用户满意度
     */
    private Double userSatisfaction;
    
    /**
     * 点击率 (Click Through Rate)
     */
    private Double clickThroughRate;
    
    /**
     * 转化率 (Conversion Rate)
     */
    private Double conversionRate;
    
    /**
     * 计算F1分数
     */
    public void calculateF1Score() {
        if (precision != null && recall != null && precision > 0 && recall > 0) {
            this.f1Score = 2 * (precision * recall) / (precision + recall);
        } else {
            this.f1Score = 0.0;
        }
    }
    
    /**
     * 计算综合评分
     * 基于各项指标的加权平均
     */
    public Double calculateOverallScore() {
        double score = 0.0;
        int count = 0;
        
        if (precision != null) {
            score += precision * 0.2;
            count++;
        }
        
        if (recall != null) {
            score += recall * 0.2;
            count++;
        }
        
        if (f1Score != null) {
            score += f1Score * 0.15;
            count++;
        }
        
        if (ndcg != null) {
            score += ndcg * 0.15;
            count++;
        }
        
        if (diversity != null) {
            score += diversity * 0.1;
            count++;
        }
        
        if (novelty != null) {
            score += novelty * 0.1;
            count++;
        }
        
        if (coverage != null) {
            score += coverage * 0.1;
            count++;
        }
        
        return count > 0 ? score : 0.0;
    }
    
    /**
     * 判断推荐质量是否良好
     */
    public boolean isGoodQuality() {
        Double overallScore = calculateOverallScore();
        return overallScore != null && overallScore >= 0.7;
    }
    
    /**
     * 获取评估报告
     */
    public String getEvaluationReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== 推荐算法评估报告 ===\n");
        
        if (precision != null) {
            report.append(String.format("准确率 (Precision): %.4f\n", precision));
        }
        
        if (recall != null) {
            report.append(String.format("召回率 (Recall): %.4f\n", recall));
        }
        
        if (f1Score != null) {
            report.append(String.format("F1分数: %.4f\n", f1Score));
        }
        
        if (mrr != null) {
            report.append(String.format("平均倒数排名 (MRR): %.4f\n", mrr));
        }
        
        if (ndcg != null) {
            report.append(String.format("NDCG: %.4f\n", ndcg));
        }
        
        if (coverage != null) {
            report.append(String.format("覆盖率: %.4f\n", coverage));
        }
        
        if (diversity != null) {
            report.append(String.format("多样性: %.4f\n", diversity));
        }
        
        if (novelty != null) {
            report.append(String.format("新颖性: %.4f\n", novelty));
        }
        
        if (timeliness != null) {
            report.append(String.format("时效性: %.4f\n", timeliness));
        }
        
        if (coldStartPerformance != null) {
            report.append(String.format("冷启动性能: %.4f\n", coldStartPerformance));
        }
        
        Double overallScore = calculateOverallScore();
        report.append(String.format("综合评分: %.4f\n", overallScore));
        report.append(String.format("推荐质量: %s\n", isGoodQuality() ? "良好" : "需要改进"));
        
        return report.toString();
    }
}