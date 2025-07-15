package com.recommend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户奖励领取记录表
 * 
 * @author liujiandong
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_reward_claim_record")
public class UserRewardClaimRecord {

    /**
     * 记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @TableField("user_id")
    private Long userId;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空")
    @TableField("activity_id")
    private Long activityId;

    /**
     * 活动日期
     */
    @NotNull(message = "活动日期不能为空")
    @TableField("activity_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate activityDate;

    /**
     * 领取的档位等级
     */
    @NotNull(message = "档位等级不能为空")
    @Positive(message = "档位等级必须大于0")
    @TableField("tier_level")
    private Integer tierLevel;

    /**
     * 该档位要求的充值金额（单位：分）
     */
    @NotNull(message = "要求充值金额不能为空")
    @TableField("required_amount")
    private Long requiredAmount;

    /**
     * 奖励类型：1-游戏币，2-道具，3-礼包，4-其他
     */
    @NotNull(message = "奖励类型不能为空")
    @TableField("reward_type")
    private Integer rewardType;

    /**
     * 实际领取的奖励内容，JSON格式存储
     */
    @NotNull(message = "奖励内容不能为空")
    @TableField("reward_content")
    private String rewardContent;

    /**
     * 领取时间
     */
    @TableField("claim_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime claimTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
} 