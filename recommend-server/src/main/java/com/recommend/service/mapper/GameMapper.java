package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.Game;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GameMapper extends BaseMapper<Game> {
    
    /**
     * 搜索游戏
     */
    List<Game> searchGames(@Param("keyword") String keyword, @Param("status") Integer status);
    
    /**
     * 根据类型获取游戏列表
     */
    List<Game> selectByType(@Param("type") String type);
    
    /**
     * 根据状态获取游戏列表
     */
    List<Game> selectByStatus(@Param("status") Integer status);
} 