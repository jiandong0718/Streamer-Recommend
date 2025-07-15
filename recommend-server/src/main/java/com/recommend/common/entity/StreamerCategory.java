package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("streamer_category")
public class StreamerCategory {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long streamerId;
    
    private Long categoryId;
    
    private Integer level;
    
    private BigDecimal score;
    
    private Integer streamCount;
    
    private Integer status;
    
    private Date createTime;
    
    private Date updateTime;
} 