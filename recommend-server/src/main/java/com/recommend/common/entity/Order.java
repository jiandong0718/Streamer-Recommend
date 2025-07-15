package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("order")
public class Order {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long streamerId;
    
    private Long categoryId;
    
    private BigDecimal amount;
    
    private Integer duration;
    
    private Integer status;
    
    private Date createTime;
    
    private Date payTime;
    
    private Date completeTime;
    
    private Date updateTime;
} 