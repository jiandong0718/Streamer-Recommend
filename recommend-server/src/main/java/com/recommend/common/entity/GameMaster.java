package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("game_master")
public class GameMaster {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String nickname;
    
    private String avatar;
    
    private String gender;
    
    private Integer age;
    
    private String region;
    
    private Integer level;
    
    private BigDecimal score;
    
    private Integer orderCount;
    
    private Integer status;
    
    private Date createTime;
    
    private Date updateTime;
} 