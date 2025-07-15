package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 主播实体类
 * 
 * @author recommend-system
 * @since 2024-01-01
 */
@Data
@TableName("streamer")
public class Streamer {
    
    /**
     * 主播ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 主播昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 性别
     */
    private String gender;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 所在地区
     */
    private String region;
    
    /**
     * 直播等级
     */
    private Integer level;
    
    /**
     * 评分
     */
    private BigDecimal score;
    
    /**
     * 直播场次数
     */
    private Integer streamCount;
    
    /**
     * 粉丝数量
     */
    private Integer fansCount;
    
    /**
     * 在线状态 (0:离线 1:在线 2:忙碌)
     */
    private Integer onlineStatus;
    
    /**
     * 直播间标题
     */
    private String roomTitle;
    
    /**
     * 直播间封面
     */
    private String roomCover;
    
    /**
     * 直播分类
     */
    private String category;
    
    /**
     * 账号状态 (0:禁用 1:正常 2:审核中)
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
} 