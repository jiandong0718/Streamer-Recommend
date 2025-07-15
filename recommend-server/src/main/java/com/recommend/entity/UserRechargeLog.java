package com.recommend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * 用户充值流水表
 * 
 * @author liujiandong
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_recharge_log")
public class UserRechargeLog {

    /**
     * 流水ID
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
     * 订单ID
     */
    @NotBlank(message = "订单ID不能为空")
    @TableField("order_id")
    private String orderId;

    /**
     * 充值金额（单位：分）
     */
    @NotNull(message = "充值金额不能为空")
    @Positive(message = "充值金额必须大于0")
    @TableField("recharge_amount")
    private Long rechargeAmount;

    /**
     * 充值时间
     */
    @NotNull(message = "充值时间不能为空")
    @TableField("recharge_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rechargeTime;

    /**
     * 状态：1-成功，0-失败
     */
    @TableField("status")
    private Integer status;

    /**
     * 充值产品类型
     */
    @TableField("product_type")
    private String productType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 充值状态枚举
     */
    public enum RechargeStatus {
        FAILED(0, "失败"),
        SUCCESS(1, "成功");

        private final Integer code;
        private final String desc;

        RechargeStatus(Integer code, String desc) {
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