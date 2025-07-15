package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("user_game_master_interaction")
public class UserGameMasterInteraction {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long masterId;
    
    // 交互特征
    private Integer watchTime;
    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;
    private BigDecimal consumption;
    private Double rating;
    private String feedback;
    
    // 匹配特征
    private Double gameTypeMatch;
    private Double levelMatch;
    private Double priceMatch;
    
    private Date createTime;
    private Date updateTime;
} 