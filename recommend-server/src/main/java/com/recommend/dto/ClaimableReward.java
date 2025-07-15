package com.recommend.dto;

import com.recommend.entity.DailyRechargeTierConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 可领取奖励DTO
 * 
 * @author liujiandong
 * @date 2024-01-01
 */
@Data
@NoArgsConstructor
public class ClaimableReward {

    /**
     * 档位等级
     */
    private Integer tierLevel;

    /**
     * 需要充值的金额（单位：分）
     */
    private Long requiredAmount;

    /**
     * 需要充值的金额（单位：元）
     */
    private BigDecimal requiredAmountYuan;

    /**
     * 奖励类型：1-游戏币，2-道具，3-礼包，4-其他
     */
    private Integer rewardType;

    /**
     * 奖励内容，JSON格式
     */
    private String rewardContent;

    /**
     * 奖励描述
     */
    private String rewardDescription;

    /**
     * 是否可领取
     */
    private Boolean canClaim;

    /**
     * 排序字段
     */
    private Integer sortOrder;

    public ClaimableReward(DailyRechargeTierConfig tierConfig) {
        this.tierLevel = tierConfig.getTierLevel();
        this.requiredAmount = tierConfig.getRequiredAmount();
        this.requiredAmountYuan = new BigDecimal(tierConfig.getRequiredAmount()).divide(new BigDecimal(100));
        this.rewardType = tierConfig.getRewardType();
        this.rewardContent = tierConfig.getRewardContent();
        this.rewardDescription = tierConfig.getRewardDescription();
        this.sortOrder = tierConfig.getSortOrder();
        this.canClaim = true; // 默认可领取，业务逻辑中会根据实际情况调整
    }
} 