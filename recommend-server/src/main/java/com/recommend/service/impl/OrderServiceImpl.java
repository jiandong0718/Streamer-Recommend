package com.recommend.service.impl;

import com.recommend.common.entity.Order;
import com.recommend.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author liujiandong
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public Order getOrderById(Long orderId) {
        return null;
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return Collections.emptyList();
    }

    @Override
    public List<Order> getOrdersByMasterId(Long masterId) {
        return Collections.emptyList();
    }

    @Override
    public List<Order> getOrdersByGameId(Long gameId) {
        return Collections.emptyList();
    }

    @Override
    public List<Order> getOrdersByStatus(Integer status) {
        return Collections.emptyList();
    }

    @Override
    public List<Order> getOrdersByTimeRange(Date startTime, Date endTime) {
        return Collections.emptyList();
    }

    @Override
    public List<Order> getOrdersByAmountRange(Double minAmount, Double maxAmount) {
        return Collections.emptyList();
    }

    @Override
    public void createOrder(Order order) {

    }

    @Override
    public void updateOrder(Order order) {

    }

    @Override
    public void updateOrderStatus(Long orderId, Integer status) {

    }

    @Override
    public void deleteOrder(Long orderId) {

    }

    @Override
    public Integer countOrdersByUserId(Long userId) {
        return 0;
    }

    @Override
    public Integer countOrdersByMasterId(Long masterId) {
        return 0;
    }

    @Override
    public Integer countOrdersByGameId(Long gameId) {
        return 0;
    }
}