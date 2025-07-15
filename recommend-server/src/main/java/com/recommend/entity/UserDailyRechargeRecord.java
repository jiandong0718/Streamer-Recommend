package com.recommend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户每日充值返利参与记录表
 * 
 * @author liujiandong
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_daily_recharge_record")
public class UserDailyRechargeRecord {

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
     * 当日累计充值金额（单位：分）
     */
    @TableField("total_recharge_amount")
    private Long totalRechargeAmount;

    /**
     * 当前达到的最高档位等级
     */
    @TableField("max_reached_tier")
    private Integer maxReachedTier;

    /**
     * 最后一次充值时间
     */
    @TableField("last_recharge_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastRechargeTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
} 