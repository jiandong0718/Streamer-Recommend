package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 主播画像宽表
 * 用于存储主播的所有特征数据，包括基础特征、专业特征、标签特征等
 * 使用HBase存储，rowkey为streamerId
 */
@Data
@TableName("streamer_wide")
public class StreamerWide {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 主播ID
     */
    private Long streamerId;
    
    // 基础特征
    private Integer age;
    private String gender;
    private String region;
    private Date registerTime;
    private String nickname;
    private String avatar;
    private String introduction;
    
    // 专业特征
    private String categories;
    private String streamLevel;
    private Double interactionRate;
    private Integer totalViewers;
    private Double engagementRate;
    private Double rating;
    private Integer completedStreams;
    private BigDecimal giftPrice;
    private String priceRange;
    
    // 实时特征
    private Integer onlineStatus;
    private Integer currentViewers;
    private String currentCategory;
    
    // 标签特征（JSON格式存储）
    private String streamerTags;
    
    // 技能特征（JSON格式存储）
    private String talents;
    
    // 统计特征
    private Integer totalFans;
    private Integer totalLikes;
    private Integer totalComments;
    private Integer totalShares;
    private Double averageResponseTime;
    private Integer totalStreamTime;
    
    // 时间特征
    private Date createTime;
    private Date updateTime;
    
    /**
     * 特征版本号：用于特征更新和回滚
     */
    private Integer version;
    
    /**
     * 特征状态：0-无效，1-有效
     */
    private Integer status;
} 