package com.recommend.service;

import com.recommend.common.entity.GameMasterTag;
import java.util.List;

/**
 * 游戏陪玩标签服务接口
 * @author liujiandong
 */
public interface GameMasterTagService {
    
    /**
     * 根据ID获取游戏陪玩标签
     */
    GameMasterTag getGameMasterTagById(Long id);
    
    /**
     * 根据陪玩ID获取标签列表
     */
    List<GameMasterTag> getGameMasterTagsByMasterId(Long masterId);
    
    /**
     * 根据标签ID获取陪玩列表
     */
    List<GameMasterTag> getGameMasterTagsByTagId(Long tagId);
    
    /**
     * 根据陪玩ID和标签ID获取游戏陪玩标签
     */
    GameMasterTag getGameMasterTagByMasterIdAndTagId(Long masterId, Long tagId);
    
    /**
     * 添加游戏陪玩标签
     */
    void addGameMasterTag(GameMasterTag gameMasterTag);
    
    /**
     * 更新游戏陪玩标签
     */
    void updateGameMasterTag(GameMasterTag gameMasterTag);
    
    /**
     * 删除游戏陪玩标签
     */
    void deleteGameMasterTag(Long masterId, Long tagId);

    void deleteGameMasterTag(Long masterId);

    List<GameMasterTag> getGameMasterTagsByWeightRange(Double minWeight, Double maxWeight);

    void updateGameMasterTagWeight(Long masterId, Long tagId, Double weight);

    /**
     * 批量添加游戏陪玩标签
     */
    void batchAddGameMasterTags(Long masterId, List<Long> tagIds);

    void batchAddGameMasterTags(List<GameMasterTag> gameMasterTags);
    
    /**
     * 批量删除游戏陪玩标签
     */
    void batchDeleteGameMasterTags(Long masterId, List<Long> tagIds);

    void batchDeleteGameMasterTags(List<Long> masterIds);
} 