package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("user_streamer_interaction")
public class UserStreamerInteraction {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long streamerId;
    
    // 交互特征
    private Integer watchTime;
    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;
    private BigDecimal giftAmount;
    private Double rating;
    private String feedback;
    
    // 匹配特征
    private Double categoryMatch;
    private Double levelMatch;
    private Double styleMatch;
    
    private Date createTime;
    private Date updateTime;
} 