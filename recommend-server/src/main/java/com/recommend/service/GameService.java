package com.recommend.service;

import com.recommend.common.entity.Game;
import java.util.List;

public interface GameService {
    
    /**
     * 根据ID获取游戏信息
     */
    Game getGameById(Long gameId);
    
    /**
     * 获取游戏列表
     */
    List<Game> getGameList();
    
    /**
     * 搜索游戏
     */
    List<Game> searchGames(String keyword, Integer status);
    
    /**
     * 创建游戏
     */
    void createGame(Game game);
    
    /**
     * 更新游戏信息
     */
    void updateGame(Game game);
    
    /**
     * 删除游戏
     */
    void deleteGame(Long gameId);
    
    /**
     * 获取游戏的陪玩师列表
     */
    List<String> getGameMasters(Long gameId);
    
    /**
     * 获取游戏的订单列表
     */
    List<String> getGameOrders(Long gameId);
    
    /**
     * 推荐游戏
     */
    List<Game> recommendGames(Long userId, Integer limit);
} 