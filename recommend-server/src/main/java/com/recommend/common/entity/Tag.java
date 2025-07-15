package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 标签实体类
 * 用于存储系统中所有可用的标签定义
 */
@Data
@TableName("tag")
public class Tag {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 标签名称
     */
    private String name;
    
    /**
     * 标签类型：1-用户标签，2-陪玩师标签，3-游戏标签
     */
    private String type;
    
    /**
     * 标签分类：如游戏类型、性格特征、技能水平等
     */
    private String category;
    
    /**
     * 标签权重：用于计算标签重要性
     */
    private BigDecimal weight;
    
    /**
     * 标签描述
     */
    private String description;
    
    /**
     * 标签状态：0-禁用，1-启用
     */
    private Integer status;
    
    private Date createTime;
    private Date updateTime;
} 