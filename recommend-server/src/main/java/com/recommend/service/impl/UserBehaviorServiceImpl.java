package com.recommend.service.impl;

import com.recommend.common.entity.UserBehavior;
import com.recommend.service.UserBehaviorService;
import com.recommend.service.mapper.UserBehaviorMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户行为服务实现类
 * @author liujiandong
 */
@Service
@Slf4j
public class UserBehaviorServiceImpl implements UserBehaviorService {

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;

    @Override
    public List<UserBehavior> getUserBehaviorsByUserId(Long userId) {
        try {
            return userBehaviorMapper.selectList(
                new LambdaQueryWrapper<UserBehavior>()
                    .eq(UserBehavior::getUserId, userId)
                    .orderByDesc(UserBehavior::getCreateTime)
            );
        } catch (Exception e) {
            log.error("获取用户行为失败，用户ID: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserBehavior> getUserBehaviorsByType(Integer type) {
        try {
            return userBehaviorMapper.selectList(
                new LambdaQueryWrapper<UserBehavior>()
                    .eq(UserBehavior::getType, type)
                    .orderByDesc(UserBehavior::getCreateTime)
            );
        } catch (Exception e) {
            log.error("获取指定类型用户行为失败，类型: {}", type, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserBehavior> getUserBehaviorsByTarget(Long targetId, Integer targetType) {
        try {
            return userBehaviorMapper.selectList(
                new LambdaQueryWrapper<UserBehavior>()
                    .eq(UserBehavior::getTargetId, targetId)
                    .eq(UserBehavior::getTargetType, targetType)
                    .orderByDesc(UserBehavior::getCreateTime)
            );
        } catch (Exception e) {
            log.error("获取目标行为失败，目标ID: {}, 目标类型: {}", targetId, targetType, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserBehavior> getUserBehaviorsByTimeRange(Date startTime, Date endTime) {
        try {
            return userBehaviorMapper.selectList(
                new LambdaQueryWrapper<UserBehavior>()
                    .between(UserBehavior::getCreateTime, startTime, endTime)
                    .orderByDesc(UserBehavior::getCreateTime)
            );
        } catch (Exception e) {
            log.error("获取时间范围内用户行为失败，开始时间: {}, 结束时间: {}", startTime, endTime, e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public void addUserBehavior(UserBehavior userBehavior) {
        try {
            userBehavior.setCreateTime(new Date());
            userBehaviorMapper.insert(userBehavior);
        } catch (Exception e) {
            log.error("添加用户行为失败，用户ID: {}", userBehavior.getUserId(), e);
            throw new RuntimeException("添加用户行为失败", e);
        }
    }

    @Override
    @Transactional
    public void batchAddUserBehaviors(List<UserBehavior> userBehaviors) {
        try {
            Date now = new Date();
            for (UserBehavior behavior : userBehaviors) {
                behavior.setCreateTime(now);
                userBehaviorMapper.insert(behavior);
            }
        } catch (Exception e) {
            log.error("批量添加用户行为失败", e);
            throw new RuntimeException("批量添加用户行为失败", e);
        }
    }

    @Override
    public Integer countUserBehaviorsByUserIdAndType(Long userId, Integer type) {
        try {
            return Math.toIntExact(userBehaviorMapper.selectCount(
                new LambdaQueryWrapper<UserBehavior>()
                    .eq(UserBehavior::getUserId, userId)
                    .eq(UserBehavior::getType, type)
            ));
        } catch (Exception e) {
            log.error("统计用户行为数量失败，用户ID: {}, 类型: {}", userId, type, e);
            return 0;
        }
    }

    @Override
    public Integer countUserBehaviorsByTargetAndType(Long targetId, Integer targetType, Integer type) {
        try {
            return Math.toIntExact(userBehaviorMapper.selectCount(
                new LambdaQueryWrapper<UserBehavior>()
                    .eq(UserBehavior::getTargetId, targetId)
                    .eq(UserBehavior::getTargetType, targetType)
                    .eq(UserBehavior::getType, type)
            ));
        } catch (Exception e) {
            log.error("统计目标行为数量失败，目标ID: {}, 目标类型: {}, 行为类型: {}", targetId, targetType, type, e);
            return 0;
        }
    }
}