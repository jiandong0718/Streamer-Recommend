package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
    
    /**
     * 根据用户ID查询用户画像
     */
    UserProfile selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据地区查询用户画像列表
     */
    List<UserProfile> selectByRegion(@Param("region") String region);
    
    /**
     * 根据年龄范围查询用户画像列表
     */
    List<UserProfile> selectByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
    
    /**
     * 根据性别查询用户画像列表
     */
    List<UserProfile> selectByGender(@Param("gender") String gender);
    
    /**
     * 更新用户画像
     */
    void updateByUserId(@Param("userId") Long userId, @Param("userProfile") UserProfile userProfile);
} 