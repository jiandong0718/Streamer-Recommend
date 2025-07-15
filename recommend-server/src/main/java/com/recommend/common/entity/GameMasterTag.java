package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 陪玩师标签关联表
 * 用于存储陪玩师与标签的关联关系
 */
@Data
@TableName("game_master_tag")
public class GameMasterTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 陪玩师ID
     */
    private Long masterId;
    
    /**
     * 标签ID
     */
    private Long tagId;
    
    /**
     * 标签权重：陪玩师在该标签上的权重值
     */
    private BigDecimal weight;
    
    /**
     * 标签来源：1-系统计算，2-陪玩师选择，3-运营设置
     */
    private Integer source;
    
    /**
     * 标签状态：0-无效，1-有效
     */
    private Integer status;
    
    private Date createTime;
    private Date updateTime;
} 