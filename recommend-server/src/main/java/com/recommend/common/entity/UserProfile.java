package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("user_profile")
public class UserProfile {
    @TableId(type = IdType.AUTO)
    private Long id;
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

    private String tags;
    
    private Date createTime;
    private Date updateTime;
} 