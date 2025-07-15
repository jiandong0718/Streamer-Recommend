package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.UserTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserTagMapper extends BaseMapper<UserTag> {
    
    /**
     * 根据用户ID查询标签列表
     */
    List<UserTag> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据标签ID查询用户列表
     */
    List<UserTag> selectByTagId(@Param("tagId") Long tagId);
    
    /**
     * 根据权重范围查询用户标签列表
     */
    List<UserTag> selectByWeightRange(@Param("minWeight") Double minWeight, @Param("maxWeight") Double maxWeight);
    
    /**
     * 更新用户标签权重
     */
    void updateWeight(@Param("userId") Long userId, @Param("tagId") Long tagId, @Param("weight") Double weight);
    
    /**
     * 删除用户标签
     */
    void deleteByUserIdAndTagId(@Param("userId") Long userId, @Param("tagId") Long tagId);
} 