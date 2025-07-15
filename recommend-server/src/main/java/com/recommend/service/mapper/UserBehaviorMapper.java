package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.UserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

@Mapper
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {
    
    /**
     * 根据用户ID查询行为列表
     */
    List<UserBehavior> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据行为类型查询行为列表
     */
    List<UserBehavior> selectByType(@Param("type") Integer type);
    
    /**
     * 根据目标ID和目标类型查询行为列表
     */
    List<UserBehavior> selectByTarget(@Param("targetId") Long targetId, @Param("targetType") Integer targetType);
    
    /**
     * 根据时间范围查询行为列表
     */
    List<UserBehavior> selectByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
    
    /**
     * 统计用户行为数量
     */
    Integer countByUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);
    
    /**
     * 统计目标行为数量
     */
    Integer countByTargetAndType(@Param("targetId") Long targetId, @Param("targetType") Integer targetType, @Param("type") Integer type);
} 