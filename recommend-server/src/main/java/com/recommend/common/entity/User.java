package com.recommend.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("user")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String nickname;
    
    private String avatar;
    
    private String gender;
    
    private Integer age;
    
    private String region;
    
    private Date registerTime;
    
    private Date lastLoginTime;
    
    private Integer status;
    
    private Date createTime;
    
    private Date updateTime;
} 