package com.recommend.service.impl;

import com.recommend.common.entity.UserBehavior;
import com.recommend.service.UserBehaviorService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author liujiandong
 */
@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {

    @Override
    public List<UserBehavior> getUserBehaviorsByUserId(Long userId) {
        return Collections.emptyList();
    }

    @Override
    public List<UserBehavior> getUserBehaviorsByType(Integer type) {
        return Collections.emptyList();
    }

    @Override
    public List<UserBehavior> getUserBehaviorsByTarget(Long targetId, Integer targetType) {
        return Collections.emptyList();
    }

    @Override
    public List<UserBehavior> getUserBehaviorsByTimeRange(Date startTime, Date endTime) {
        return Collections.emptyList();
    }

    @Override
    public void addUserBehavior(UserBehavior userBehavior) {

    }

    @Override
    public void batchAddUserBehaviors(List<UserBehavior> userBehaviors) {

    }

    @Override
    public Integer countUserBehaviorsByUserIdAndType(Long userId, Integer type) {
        return 0;
    }

    @Override
    public Integer countUserBehaviorsByTargetAndType(Long targetId, Integer targetType, Integer type) {
        return 0;
    }
}