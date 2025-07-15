package com.recommend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户活动数据概览DTO
 * 
 * @author liujiandong
 * @date 2024-01-01
 */
@Data
public class UserActivityOverview {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动日期
     */
    private LocalDate activityDate;

    /**
     * 当日累计充值金额（单位：分）
     */
    private Long totalRechargeAmount;

    /**
     * 当日累计充值金额（单位：元）
     */
    private BigDecimal totalRechargeAmountYuan;

    /**
     * 当前达到的最高档位等级
     */
    private Integer maxReachedTier;

    /**
     * 总档位数
     */
    private Integer totalTiers;

    /**
     * 最后一次充值时间
     */
    private LocalDateTime lastRechargeTime;

    /**
     * 可领取的奖励列表
     */
    private List<ClaimableReward> claimableRewards;

    /**
     * 已领取的档位数量
     */
    private Integer claimedTierCount;

    /**
     * 未领取的档位数量
     */
    private Integer unclaimedTierCount;

    /**
     * 下一个档位信息
     */
    private NextTierInfo nextTier;

    /**
     * 活动是否进行中
     */
    private Boolean activityActive;

    /**
     * 下一个档位信息
     */
    @Data
    public static class NextTierInfo {
        /**
         * 下一档位等级
         */
        private Integer tierLevel;

        /**
         * 下一档位需要的总充值金额（单位：分）
         */
        private Long requiredAmount;

        /**
         * 下一档位需要的总充值金额（单位：元）
         */
        private BigDecimal requiredAmountYuan;

        /**
         * 还需要充值的金额（单位：分）
         */
        private Long needAmount;

        /**
         * 还需要充值的金额（单位：元）
         */
        private BigDecimal needAmountYuan;

        /**
         * 奖励描述
         */
        private String rewardDescription;

        /**
         * 进度百分比
         */
        private Double progressPercentage;
    }

    /**
     * 计算金额转换
     */
    public void calculateAmountConversion() {
        if (totalRechargeAmount != null) {
            this.totalRechargeAmountYuan = new BigDecimal(totalRechargeAmount).divide(new BigDecimal(100));
        }

        if (nextTier != null && nextTier.getRequiredAmount() != null) {
            nextTier.setRequiredAmountYuan(new BigDecimal(nextTier.getRequiredAmount()).divide(new BigDecimal(100)));
            
            if (nextTier.getNeedAmount() != null) {
                nextTier.setNeedAmountYuan(new BigDecimal(nextTier.getNeedAmount()).divide(new BigDecimal(100)));
            }

            // 计算进度百分比
            if (totalRechargeAmount != null && nextTier.getRequiredAmount() > 0) {
                double progress = (double) totalRechargeAmount / nextTier.getRequiredAmount() * 100;
                nextTier.setProgressPercentage(Math.min(progress, 100.0));
            }
        }
    }
} 