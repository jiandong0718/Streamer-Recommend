package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("game_master")
public class GameMaster {
    
    @Data
    public static class Game {
        private String gameType;
        private String gameName;
        private Integer proficiency;
    }
    
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
    
    private BigDecimal price;
    
    private String gameTypes;
    
    private List<Game> games;

    private String tags;  // 改为String类型，存储逗号分隔的标签
    
    private Date createTime;
    
    private Date updateTime;
} 