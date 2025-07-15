package com.recommend.algorithm;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class RecommendMetrics {
    /**
     * 准确率（Precision）
     */
    private double precision;
    
    /**
     * 召回率（Recall）
     */
    private double recall;
    
    /**
     * F1分数
     */
    private double f1Score;
    
    /**
     * 平均倒数排名（Mean Reciprocal Rank）
     */
    private double mrr;
    
    /**
     * 归一化折损累积增益（Normalized Discounted Cumulative Gain）
     */
    private double ndcg;
    
    /**
     * 覆盖率（Coverage）
     */
    private double coverage;
    
    /**
     * 多样性（Diversity）
     */
    private double diversity;
    
    /**
     * 新颖性（Novelty）
     */
    private double novelty;
    
    /**
     * 实时性（Timeliness）
     */
    private double timeliness;
    
    /**
     * 冷启动性能（Cold Start Performance）
     */
    private double coldStartPerformance;
    
    /**
     * 计算F1分数
     */
    public void calculateF1Score() {
        if (precision + recall > 0) {
            this.f1Score = 2 * precision * recall / (precision + recall);
        } else {
            this.f1Score = 0.0;
        }
    }
    
    /**
     * 计算综合得分
     * @return 综合得分
     */
    public double calculateOverallScore() {
        return (precision * 0.2 +
                recall * 0.2 +
                f1Score * 0.1 +
                mrr * 0.1 +
                ndcg * 0.1 +
                coverage * 0.1 +
                diversity * 0.1 +
                novelty * 0.05 +
                timeliness * 0.05 +
                coldStartPerformance * 0.05);
    }
} 