package com.recommend.service;

import com.recommend.common.entity.UserBehavior;
import java.util.List;
import java.util.Date;

public interface UserBehaviorService {
    
    /**
     * 根据用户ID获取行为列表
     */
    List<UserBehavior> getUserBehaviorsByUserId(Long userId);
    
    /**
     * 根据行为类型获取行为列表
     */
    List<UserBehavior> getUserBehaviorsByType(Integer type);
    
    /**
     * 根据目标ID和目标类型获取行为列表
     */
    List<UserBehavior> getUserBehaviorsByTarget(Long targetId, Integer targetType);
    
    /**
     * 根据时间范围获取行为列表
     */
    List<UserBehavior> getUserBehaviorsByTimeRange(Date startTime, Date endTime);
    
    /**
     * 添加用户行为
     */
    void addUserBehavior(UserBehavior userBehavior);
    
    /**
     * 批量添加用户行为
     */
    void batchAddUserBehaviors(List<UserBehavior> userBehaviors);
    
    /**
     * 统计用户行为数量
     */
    Integer countUserBehaviorsByUserIdAndType(Long userId, Integer type);
    
    /**
     * 统计目标行为数量
     */
    Integer countUserBehaviorsByTargetAndType(Long targetId, Integer targetType, Integer type);
} 