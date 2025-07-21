package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 主播订单表
 */
@Data
@TableName("order")
public class Order {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 主播ID
     */
    private Long streamerId;

    private Long gameId;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 订单金额
     */
    private BigDecimal amount;
    
    /**
     * 订单时长(分钟)
     */
    private Integer duration;
    
    /**
     * 订单状态：0-待支付，1-已支付，2-已完成，3-已取消，4-已退款
     */
    private Integer status;
    
    /**
     * 订单类型：1-礼物打赏，2-付费连麦，3-专属会员
     */
    private Integer type;
    
    /**
     * 用户评分(1-5星)
     */
    private Double rating;
    
    /**
     * 用户评价
     */
    private String comment;
    
    /**
     * 订单备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 支付时间
     */
    private Date payTime;
    
    /**
     * 完成时间
     */
    private Date completeTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 获取陪玩师ID（兼容性方法）
     * @return 陪玩师ID
     */
    public Long getMasterId() {
        return this.streamerId;
    }
    
    /**
     * 设置陪玩师ID（兼容性方法）
     * @param masterId 陪玩师ID
     */
    public void setMasterId(Long masterId) {
        this.streamerId = masterId;
    }
} 