package com.recommend.dto;

import lombok.Data;

import java.util.List;

/**
 * 奖励发放结果DTO
 * 
 * @author liujiandong
 * @date 2024-01-01
 */
@Data
public class RewardResult {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 消息
     */
    private String message;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 发放的奖励列表
     */
    private List<RewardClaimResult.RewardItem> rewards;

    /**
     * 发放流水号
     */
    private String transactionId;

    /**
     * 发放时间戳
     */
    private Long timestamp;

    private RewardResult() {}

    public static RewardResult success(List<RewardClaimResult.RewardItem> rewards) {
        RewardResult result = new RewardResult();
        result.success = true;
        result.message = "奖励发放成功";
        result.rewards = rewards;
        result.timestamp = System.currentTimeMillis();
        return result;
    }

    public static RewardResult success(List<RewardClaimResult.RewardItem> rewards, String transactionId) {
        RewardResult result = success(rewards);
        result.transactionId = transactionId;
        return result;
    }

    public static RewardResult fail(String message) {
        RewardResult result = new RewardResult();
        result.success = false;
        result.message = message;
        result.timestamp = System.currentTimeMillis();
        return result;
    }

    public static RewardResult fail(String message, String errorCode) {
        RewardResult result = fail(message);
        result.errorCode = errorCode;
        return result;
    }
} 