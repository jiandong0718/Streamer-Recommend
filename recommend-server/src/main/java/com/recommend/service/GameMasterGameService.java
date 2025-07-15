package com.recommend.service;

import com.recommend.common.entity.GameMasterGame;
import java.util.List;

/**
 * @author liujiandong
 */
public interface GameMasterGameService {
    
    /**
     * 根据陪玩ID获取游戏列表
     */
    List<GameMasterGame> getGameMasterGamesByMasterId(Long masterId);
    
    /**
     * 根据游戏ID获取陪玩列表
     */
    List<GameMasterGame> getGameMasterGamesByGameId(Long gameId);
    
    /**
     * 根据评分范围获取陪玩游戏列表
     */
    List<GameMasterGame> getGameMasterGamesByScoreRange(Double minScore, Double maxScore);
    
    /**
     * 根据订单数量范围获取陪玩游戏列表
     */
    List<GameMasterGame> getGameMasterGamesByOrderCountRange(Integer minOrderCount, Integer maxOrderCount);
    
    /**
     * 添加陪玩游戏
     */
    void addGameMasterGame(GameMasterGame gameMasterGame);
    
    /**
     * 更新陪玩游戏评分
     */
    void updateGameMasterGameScore(Long masterId, Long gameId, Double score);
    
    /**
     * 更新陪玩游戏订单数量
     */
    void updateGameMasterGameOrderCount(Long masterId, Long gameId, Integer orderCount);
    
    /**
     * 删除陪玩游戏
     */
    void deleteGameMasterGame(Long masterId, Long gameId);
    
    /**
     * 批量添加陪玩游戏
     */
    void batchAddGameMasterGames(List<GameMasterGame> gameMasterGames);
    
    /**
     * 批量删除陪玩游戏
     */
    void batchDeleteGameMasterGames(Long masterId, List<Long> gameIds);
} 