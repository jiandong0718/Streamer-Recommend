package com.recommend.service;

import com.recommend.common.entity.Order;
import java.util.List;
import java.util.Date;

/**
 * @author liujiandong
 */
public interface OrderService {
    
    /**
     * 根据ID获取订单信息
     */
    Order getOrderById(Long orderId);
    
    /**
     * 根据用户ID获取订单列表
     */
    List<Order> getOrdersByUserId(Long userId);
    
    /**
     * 根据陪玩ID获取订单列表
     */
    List<Order> getOrdersByMasterId(Long masterId);
    
    /**
     * 根据游戏ID获取订单列表
     */
    List<Order> getOrdersByGameId(Long gameId);
    
    /**
     * 根据状态获取订单列表
     */
    List<Order> getOrdersByStatus(Integer status);
    
    /**
     * 根据时间范围获取订单列表
     */
    List<Order> getOrdersByTimeRange(Date startTime, Date endTime);
    
    /**
     * 根据金额范围获取订单列表
     */
    List<Order> getOrdersByAmountRange(Double minAmount, Double maxAmount);
    
    /**
     * 创建订单
     */
    void createOrder(Order order);
    
    /**
     * 更新订单信息
     */
    void updateOrder(Order order);
    
    /**
     * 更新订单状态
     */
    void updateOrderStatus(Long orderId, Integer status);
    
    /**
     * 删除订单
     */
    void deleteOrder(Long orderId);
    
    /**
     * 统计用户订单数量
     */
    Integer countOrdersByUserId(Long userId);
    
    /**
     * 统计陪玩订单数量
     */
    Integer countOrdersByMasterId(Long masterId);
    
    /**
     * 统计游戏订单数量
     */
    Integer countOrdersByGameId(Long gameId);
} 