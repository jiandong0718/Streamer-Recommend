package com.recommend.service.rank;

import com.recommend.common.entity.GameMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排序后的推荐项
 * 包含陪玩师信息和计算得分
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankedItem {
    
    /**
     * 陪玩师信息
     */
    private GameMaster master;
    
    /**
     * 综合得分
     */
    private Double score;
    
    /**
     * 相关性得分
     */
    private Double relevanceScore;
    
    /**
     * 多样性得分
     */
    private Double diversityScore;
    
    /**
     * 热门度得分
     */
    private Double popularityScore;
    
    /**
     * 排序位置
     */
    private Integer rank;
    
    /**
     * 推荐原因
     */
    private String reason;
    
    /**
     * 简化构造函数
     */
    public RankedItem(GameMaster master, Double score) {
        this.master = master;
        this.score = score;
    }
    
    /**
     * 详细构造函数
     */
    public RankedItem(GameMaster master, Double score, Double relevanceScore, 
                     Double diversityScore, Double popularityScore) {
        this.master = master;
        this.score = score;
        this.relevanceScore = relevanceScore;
        this.diversityScore = diversityScore;
        this.popularityScore = popularityScore;
    }
}