package com.recommend.algorithm;

import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.Game;
import java.util.List;

public interface RecommendAlgorithm {
    
    /**
     * 推荐游戏陪玩
     * @param userId 用户ID
     * @param gameId 游戏ID（可选）
     * @param limit 推荐数量
     * @return 推荐的游戏陪玩列表
     */
    List<GameMaster> recommendGameMasters(Long userId, Long gameId, Integer limit);
    
    /**
     * 推荐游戏
     * @param userId 用户ID
     * @param limit 推荐数量
     * @return 推荐的游戏列表
     */
    List<Game> recommendGames(Long userId, Integer limit);
    
    /**
     * 计算用户与游戏陪玩的相似度
     * @param userId 用户ID
     * @param masterId 游戏陪玩ID
     * @return 相似度分数
     */
    double calculateUserMasterSimilarity(Long userId, Long masterId);
    
    /**
     * 计算用户与游戏的相似度
     * @param userId 用户ID
     * @param gameId 游戏ID
     * @return 相似度分数
     */
    double calculateUserGameSimilarity(Long userId, Long gameId);
    
    /**
     * 计算游戏陪玩之间的相似度
     * @param masterId1 游戏陪玩ID1
     * @param masterId2 游戏陪玩ID2
     * @return 相似度分数
     */
    double calculateMasterSimilarity(Long masterId1, Long masterId2);
    
    /**
     * 计算游戏之间的相似度
     * @param gameId1 游戏ID1
     * @param gameId2 游戏ID2
     * @return 相似度分数
     */
    double calculateGameSimilarity(Long gameId1, Long gameId2);
    
    /**
     * 更新用户特征向量
     * @param userId 用户ID
     */
    void updateUserFeatures(Long userId);
    
    /**
     * 更新游戏陪玩特征向量
     * @param masterId 游戏陪玩ID
     */
    void updateMasterFeatures(Long masterId);
    
    /**
     * 更新游戏特征向量
     * @param gameId 游戏ID
     */
    void updateGameFeatures(Long gameId);
    
    /**
     * 训练推荐模型
     */
    void trainModel();
    
    /**
     * 评估推荐模型
     * @return 评估指标
     */
    RecommendMetrics evaluateModel();
} 