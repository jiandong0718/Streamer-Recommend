package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
    /**
     * 根据用户ID查询订单列表
     */
    List<Order> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据陪玩ID查询订单列表
     */
    List<Order> selectByMasterId(@Param("masterId") Long masterId);
    
    /**
     * 根据游戏ID查询订单列表
     */
    List<Order> selectByGameId(@Param("gameId") Long gameId);
    
    /**
     * 根据状态查询订单列表
     */
    List<Order> selectByStatus(@Param("status") Integer status);
    
    /**
     * 根据时间范围查询订单列表
     */
    List<Order> selectByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
    
    /**
     * 根据金额范围查询订单列表
     */
    List<Order> selectByAmountRange(@Param("minAmount") Double minAmount, @Param("maxAmount") Double maxAmount);
    
    /**
     * 统计用户订单数量
     */
    Integer countByUserId(@Param("userId") Long userId);
    
    /**
     * 统计陪玩订单数量
     */
    Integer countByMasterId(@Param("masterId") Long masterId);
    
    /**
     * 统计游戏订单数量
     */
    Integer countByGameId(@Param("gameId") Long gameId);
    
    /**
     * 更新订单状态
     */
    void updateStatus(@Param("orderId") Long orderId, @Param("status") Integer status);
} 