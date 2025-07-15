package com.recommend.service;

import com.recommend.common.entity.GameMaster;
import java.util.List;

public interface GameMasterService {
    
    /**
     * 根据ID获取陪玩信息
     */
    GameMaster getGameMasterById(Long masterId);
    
    /**
     * 获取陪玩列表
     */
    List<GameMaster> getGameMasterList();
    
    /**
     * 搜索陪玩
     */
    List<GameMaster> searchGameMasters(String keyword, Integer status);
    
    /**
     * 根据地区获取陪玩列表
     */
    List<GameMaster> getGameMastersByRegion(String region);
    
    /**
     * 根据状态获取陪玩列表
     */
    List<GameMaster> getGameMastersByStatus(Integer status);
    
    /**
     * 根据评分范围获取陪玩列表
     */
    List<GameMaster> getGameMastersByScoreRange(Double minScore, Double maxScore);
    
    /**
     * 根据价格范围获取陪玩列表
     */
    List<GameMaster> getGameMastersByPriceRange(Double minPrice, Double maxPrice);
    
    /**
     * 创建陪玩
     */
    void createGameMaster(GameMaster gameMaster);
    
    /**
     * 更新陪玩信息
     */
    void updateGameMaster(GameMaster gameMaster);
    
    /**
     * 更新陪玩状态
     */
    void updateGameMasterStatus(Long masterId, Integer status);
    
    /**
     * 更新陪玩评分
     */
    void updateGameMasterScore(Long masterId, Double score);
    
    /**
     * 删除陪玩
     */
    void deleteGameMaster(Long masterId);
    
    /**
     * 获取陪玩画像
     */
    GameMaster getGameMasterProfile(Long masterId);
    
    /**
     * 获取陪玩标签
     */
    List<String> getGameMasterTags(Long masterId);
    
    /**
     * 获取陪玩游戏
     */
    List<String> getGameMasterGames(Long masterId);
    
    /**
     * 获取陪玩订单
     */
    List<String> getGameMasterOrders(Long masterId);
    
    /**
     * 推荐陪玩
     */
    List<GameMaster> recommendGameMasters(Long userId, Long gameId, Integer limit);
} 