package com.recommend.service.impl;

import com.recommend.common.entity.Order;
import com.recommend.service.OrderService;
import com.recommend.service.mapper.OrderMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单服务实现类
 * @author liujiandong
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order getOrderById(Long orderId) {
        try {
            return orderMapper.selectById(orderId);
        } catch (Exception e) {
            log.error("获取订单失败，订单ID: {}", orderId, e);
            return null;
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        try {
            return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                    .eq(Order::getUserId, userId)
                    .orderByDesc(Order::getCreateTime)
            );
        } catch (Exception e) {
            log.error("获取用户订单失败，用户ID: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Order> getOrdersByMasterId(Long masterId) {
        try {
            return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                    .eq(Order::getMasterId, masterId)
                    .orderByDesc(Order::getCreateTime)
            );
        } catch (Exception e) {
            log.error("获取陪玩师订单失败，陪玩师ID: {}", masterId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Order> getOrdersByGameId(Long gameId) {
        try {
            return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                    .eq(Order::getGameId, gameId)
                    .orderByDesc(Order::getCreateTime)
            );
        } catch (Exception e) {
            log.error("获取游戏订单失败，游戏ID: {}", gameId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Order> getOrdersByStatus(Integer status) {
        try {
            return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                    .eq(Order::getStatus, status)
                    .orderByDesc(Order::getCreateTime)
            );
        } catch (Exception e) {
            log.error("获取指定状态订单失败，状态: {}", status, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Order> getOrdersByTimeRange(Date startTime, Date endTime) {
        try {
            return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                    .between(Order::getCreateTime, startTime, endTime)
                    .orderByDesc(Order::getCreateTime)
            );
        } catch (Exception e) {
            log.error("获取时间范围内订单失败，开始时间: {}, 结束时间: {}", startTime, endTime, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Order> getOrdersByAmountRange(Double minAmount, Double maxAmount) {
        try {
            return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                    .between(Order::getAmount, minAmount, maxAmount)
                    .orderByDesc(Order::getCreateTime)
            );
        } catch (Exception e) {
            log.error("获取金额范围内订单失败，最小金额: {}, 最大金额: {}", minAmount, maxAmount, e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public void createOrder(Order order) {
        try {
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            orderMapper.insert(order);
        } catch (Exception e) {
            log.error("创建订单失败，用户ID: {}, 陪玩师ID: {}", order.getUserId(), order.getMasterId(), e);
            throw new RuntimeException("创建订单失败", e);
        }
    }

    @Override
    @Transactional
    public void updateOrder(Order order) {
        try {
            order.setUpdateTime(new Date());
            orderMapper.updateById(order);
        } catch (Exception e) {
            log.error("更新订单失败，订单ID: {}", order.getId(), e);
            throw new RuntimeException("更新订单失败", e);
        }
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, Integer status) {
        try {
            Order order = new Order();
            order.setId(orderId);
            order.setStatus(status);
            order.setUpdateTime(new Date());
            orderMapper.updateById(order);
        } catch (Exception e) {
            log.error("更新订单状态失败，订单ID: {}, 状态: {}", orderId, status, e);
            throw new RuntimeException("更新订单状态失败", e);
        }
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        try {
            orderMapper.deleteById(orderId);
        } catch (Exception e) {
            log.error("删除订单失败，订单ID: {}", orderId, e);
            throw new RuntimeException("删除订单失败", e);
        }
    }

    @Override
    public Integer countOrdersByUserId(Long userId) {
        try {
            return Math.toIntExact(orderMapper.selectCount(
                new LambdaQueryWrapper<Order>()
                    .eq(Order::getUserId, userId)
            ));
        } catch (Exception e) {
            log.error("统计用户订单数量失败，用户ID: {}", userId, e);
            return 0;
        }
    }

    @Override
    public Integer countOrdersByMasterId(Long masterId) {
        try {
            return Math.toIntExact(orderMapper.selectCount(
                new LambdaQueryWrapper<Order>()
                    .eq(Order::getMasterId, masterId)
            ));
        } catch (Exception e) {
            log.error("统计陪玩师订单数量失败，陪玩师ID: {}", masterId, e);
            return 0;
        }
    }

    @Override
    public Integer countOrdersByGameId(Long gameId) {
        try {
            return Math.toIntExact(orderMapper.selectCount(
                new LambdaQueryWrapper<Order>()
                    .eq(Order::getGameId, gameId)
            ));
        } catch (Exception e) {
            log.error("统计游戏订单数量失败，游戏ID: {}", gameId, e);
            return 0;
        }
    }
}