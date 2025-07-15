package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 搜索用户
     */
    List<User> searchUsers(@Param("keyword") String keyword, @Param("status") Integer status);
    
    /**
     * 根据地区查询用户列表
     */
    List<User> selectByRegion(@Param("region") String region);
    
    /**
     * 根据状态查询用户列表
     */
    List<User> selectByStatus(@Param("status") Integer status);
    
    /**
     * 更新用户最后登录时间
     */
    void updateLastLoginTime(@Param("userId") Long userId);
    
    /**
     * 获取用户标签名称列表
     */
    List<String> selectUserTagNames(@Param("userId") Long userId);
    
    /**
     * 获取用户行为记录
     */
    List<String> selectUserBehaviors(@Param("userId") Long userId);
    
    /**
     * 获取用户订单信息
     */
    List<String> selectUserOrders(@Param("userId") Long userId);
} 