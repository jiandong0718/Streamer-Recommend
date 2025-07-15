package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 陪玩师订单表
 * @author liujiandong
 */
@Data
@TableName("game_master_order")
public class GameMasterOrder {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 陪玩师ID
     */
    private Long masterId;

    /**
     * 游戏ID
     */
    private Long gameId;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 订单状态：0-待支付，1-已支付，2-已完成，3-已取消，4-已退款
     */
    private Integer status;

    /**
     * 订单类型：1-按小时，2-按局数
     */
    private Integer type;

    /**
     * 订单数量（小时数或局数）
     */
    private Integer quantity;

    /**
     * 订单开始时间
     */
    private Date startTime;

    /**
     * 订单结束时间
     */
    private Date endTime;

    /**
     * 用户评分
     */
    private Double rating;

    /**
     * 用户评价
     */
    private String comment;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
