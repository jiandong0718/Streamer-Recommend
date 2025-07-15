package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户画像宽表
 * 用于存储用户的所有特征数据，包括基础特征、行为特征、标签特征等
 * 使用HBase存储，rowkey为userId
 */
@Data
@TableName("user_profile_wide")
public class UserProfileWide {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    // 基础特征
    private Integer age;
    private String gender;
    private String region;
    private Date registerTime;
    private String deviceType;
    private String osType;
    
    // 游戏行为特征
    private String gameTypes;
    private String playFrequency;
    private String skillLevel;
    private Integer totalGameTime;
    private Double winRate;
    private Integer totalWatchTime;
    private Integer totalInteractionCount;
    private BigDecimal totalConsumption;
    
    // 实时特征
    private Date lastActiveTime;
    private String currentGame;
    private String onlineStatus;
    
    // 标签特征（JSON格式存储）
    private String userTags;
    
    // 偏好特征（JSON格式存储）
    private String preferences;
    
    // 统计特征
    private Integer totalOrders;
    private Integer totalFavorites;
    private Integer totalFollows;
    private Double averageRating;
    private Integer totalComments;
    
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