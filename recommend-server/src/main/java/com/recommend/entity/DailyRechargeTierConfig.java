package com.recommend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * 每日充值返利档位配置表
 * 
 * @author liujiandong
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("daily_recharge_tier_config")
public class DailyRechargeTierConfig {

    /**
     * 档位配置ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空")
    @TableField("activity_id")
    private Long activityId;

    /**
     * 档位等级，从1开始
     */
    @NotNull(message = "档位等级不能为空")
    @Positive(message = "档位等级必须大于0")
    @TableField("tier_level")
    private Integer tierLevel;

    /**
     * 需要充值的金额（单位：分）
     */
    @NotNull(message = "需要充值金额不能为空")
    @Positive(message = "需要充值金额必须大于0")
    @TableField("required_amount")
    private Long requiredAmount;

    /**
     * 奖励类型：1-游戏币，2-道具，3-礼包，4-其他
     */
    @NotNull(message = "奖励类型不能为空")
    @TableField("reward_type")
    private Integer rewardType;

    /**
     * 奖励内容，JSON格式存储
     */
    @NotNull(message = "奖励内容不能为空")
    @TableField("reward_content")
    private String rewardContent;

    /**
     * 奖励描述
     */
    @TableField("reward_description")
    private String rewardDescription;

    /**
     * 排序字段
     */
    @TableField("sort_order")
    private Integer sortOrder;

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

    /**
     * 奖励类型枚举
     */
    public enum RewardType {
        GAME_COIN(1, "游戏币"),
        ITEM(2, "道具"),
        GIFT_PACK(3, "礼包"),
        OTHER(4, "其他");

        private final Integer code;
        private final String desc;

        RewardType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
} 