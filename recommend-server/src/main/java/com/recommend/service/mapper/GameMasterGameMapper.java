package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.GameMasterGame;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GameMasterGameMapper extends BaseMapper<GameMasterGame> {
    
    /**
     * 根据陪玩ID查询游戏列表
     */
    List<GameMasterGame> selectByMasterId(@Param("masterId") Long masterId);
    
    /**
     * 根据游戏ID查询陪玩列表
     */
    List<GameMasterGame> selectByGameId(@Param("gameId") Long gameId);
    
    /**
     * 根据评分范围查询陪玩游戏列表
     */
    List<GameMasterGame> selectByScoreRange(@Param("minScore") Double minScore, @Param("maxScore") Double maxScore);
    
    /**
     * 根据订单数量范围查询陪玩游戏列表
     */
    List<GameMasterGame> selectByOrderCountRange(@Param("minOrderCount") Integer minOrderCount, @Param("maxOrderCount") Integer maxOrderCount);
    
    /**
     * 更新陪玩游戏评分
     */
    void updateScore(@Param("masterId") Long masterId, @Param("gameId") Long gameId, @Param("score") Double score);
    
    /**
     * 更新陪玩游戏订单数量
     */
    void updateOrderCount(@Param("masterId") Long masterId, @Param("gameId") Long gameId, @Param("orderCount") Integer orderCount);
} 