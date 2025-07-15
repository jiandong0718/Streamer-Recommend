package com.recommend.dto;

import lombok.Data;

import java.util.List;

/**
 * 奖励领取结果DTO
 * 
 * @author liujiandong
 * @date 2024-01-01
 */
@Data
public class RewardClaimResult {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 消息
     */
    private String message;

    /**
     * 领取的奖励列表
     */
    private List<RewardItem> rewards;

    /**
     * 档位等级
     */
    private Integer tierLevel;

    /**
     * 领取时间戳
     */
    private Long claimTime;

    private RewardClaimResult() {}

    public static RewardClaimResult success(List<RewardItem> rewards) {
        RewardClaimResult result = new RewardClaimResult();
        result.success = true;
        result.message = "领取成功";
        result.rewards = rewards;
        result.claimTime = System.currentTimeMillis();
        return result;
    }

    public static RewardClaimResult success(List<RewardItem> rewards, Integer tierLevel) {
        RewardClaimResult result = success(rewards);
        result.tierLevel = tierLevel;
        return result;
    }

    public static RewardClaimResult fail(String message) {
        RewardClaimResult result = new RewardClaimResult();
        result.success = false;
        result.message = message;
        return result;
    }

    /**
     * 奖励物品
     */
    @Data
    public static class RewardItem {
        /**
         * 物品ID
         */
        private String itemId;

        /**
         * 物品名称
         */
        private String itemName;

        /**
         * 物品类型
         */
        private Integer itemType;

        /**
         * 数量
         */
        private Integer amount;

        /**
         * 物品图标
         */
        private String icon;

        /**
         * 物品描述
         */
        private String description;
    }
} 