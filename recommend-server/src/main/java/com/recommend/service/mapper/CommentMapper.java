package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    
    /**
     * 根据用户ID查询评价列表
     */
    List<Comment> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据陪玩ID查询评价列表
     */
    List<Comment> selectByMasterId(@Param("masterId") Long masterId);
    
    /**
     * 根据订单ID查询评价
     */
    Comment selectByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 根据评分范围查询评价列表
     */
    List<Comment> selectByScoreRange(@Param("minScore") Double minScore, @Param("maxScore") Double maxScore);
    
    /**
     * 根据时间范围查询评价列表
     */
    List<Comment> selectByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
    
    /**
     * 统计用户评价数量
     */
    Integer countByUserId(@Param("userId") Long userId);
    
    /**
     * 统计陪玩评价数量
     */
    Integer countByMasterId(@Param("masterId") Long masterId);
    
    /**
     * 计算陪玩平均评分
     */
    Double calculateAverageScore(@Param("masterId") Long masterId);
} 