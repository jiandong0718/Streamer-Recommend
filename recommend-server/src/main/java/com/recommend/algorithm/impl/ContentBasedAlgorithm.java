package com.recommend.algorithm.impl;

import com.recommend.algorithm.RecommendAlgorithm;
import com.recommend.algorithm.RecommendMetrics;
import com.recommend.common.entity.*;
import com.recommend.common.utils.MathUtils;
import com.recommend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

@Component
@Slf4j
public class ContentBasedAlgorithm implements RecommendAlgorithm {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GameMasterService gameMasterService;
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private UserTagService userTagService;
    
    @Autowired
    private GameMasterTagService gameMasterTagService;
    
    @Autowired
    private TagService tagService;

    @Autowired
    private UserBehaviorService userBehaviorService;

    @Autowired
    private OrderService orderService;
    
    // 用户-标签权重矩阵
    private Map<Long, Map<Long, Double>> userTagWeights;
    
    // 游戏陪玩-标签权重矩阵
    private Map<Long, Map<Long, Double>> masterTagWeights;
    
    // 游戏-标签权重矩阵
    private Map<Long, Map<Long, Double>> gameTagWeights;
    
    @Override
    public List<GameMaster> recommendGameMasters(Long userId, Long gameId, Integer limit) {
        // 1. 获取用户的标签权重
        Map<Long, Double> userTags = getUserTagWeights(userId);
        
        // 2. 计算用户与游戏陪玩的相似度
        Map<Long, Double> masterScores = new HashMap<>();
        for (Long masterId : getAllMasters()) {
            if (gameId != null && !isMasterGameMatch(masterId, gameId)) {
                continue;
            }
            double score = calculateUserMasterSimilarity(userId, masterId);
            masterScores.put(masterId, score);
        }
        
        // 3. 排序并返回推荐结果
        return masterScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> gameMasterService.getGameMasterById(entry.getKey()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Game> recommendGames(Long userId, Integer limit) {
        // 1. 获取用户的标签权重
        Map<Long, Double> userTags = getUserTagWeights(userId);
        
        // 2. 计算用户与游戏的相似度
        Map<Long, Double> gameScores = new HashMap<>();
        for (Long gameId : getAllGames()) {
            double score = calculateUserGameSimilarity(userId, gameId);
            gameScores.put(gameId, score);
        }
        
        // 3. 排序并返回推荐结果
        return gameScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> gameService.getGameById(entry.getKey()))
                .collect(Collectors.toList());
    }
    
    @Override
    public double calculateUserMasterSimilarity(Long userId, Long masterId) {
        // 1. 获取用户和游戏陪玩的标签权重
        Map<Long, Double> userTags = getUserTagWeights(userId);
        Map<Long, Double> masterTags = getMasterTagWeights(masterId);
        
        // 2. 计算标签权重的余弦相似度
        return calculateTagWeightSimilarity(userTags, masterTags);
    }
    
    @Override
    public double calculateUserGameSimilarity(Long userId, Long gameId) {
        // 1. 获取用户和游戏的标签权重
        Map<Long, Double> userTags = getUserTagWeights(userId);
        Map<Long, Double> gameTags = getGameTagWeights(gameId);
        
        // 2. 计算标签权重的余弦相似度
        return calculateTagWeightSimilarity(userTags, gameTags);
    }
    
    @Override
    public double calculateMasterSimilarity(Long masterId1, Long masterId2) {
        // 1. 获取两个游戏陪玩的标签权重
        Map<Long, Double> masterTags1 = getMasterTagWeights(masterId1);
        Map<Long, Double> masterTags2 = getMasterTagWeights(masterId2);
        
        // 2. 计算标签权重的余弦相似度
        return calculateTagWeightSimilarity(masterTags1, masterTags2);
    }
    
    @Override
    public double calculateGameSimilarity(Long gameId1, Long gameId2) {
        // 1. 获取两个游戏的标签权重
        Map<Long, Double> gameTags1 = getGameTagWeights(gameId1);
        Map<Long, Double> gameTags2 = getGameTagWeights(gameId2);
        
        // 2. 计算标签权重的余弦相似度
        return calculateTagWeightSimilarity(gameTags1, gameTags2);
    }
    
    @Override
    public void updateUserFeatures(Long userId) {
        // 1. 获取用户的标签数据
        List<Long> userTags = userTagService.getUserTagsByUserId(userId).stream()
                .map(userTag -> userTag.getTagId())
                .collect(Collectors.toList());
        
        // 2. 计算标签权重
        Map<Long, Double> tagWeights = new HashMap<>();
        for (Long tagId : userTags) {
            double weight = calculateTagWeight(userId, tagId);
            tagWeights.put(tagId, weight);
        }
        
        // 3. 更新用户-标签权重矩阵
        userTagWeights.put(userId, tagWeights);
    }
    
    @Override
    public void updateMasterFeatures(Long masterId) {
        // 1. 获取游戏陪玩的标签数据
        List<Long> masterTags = gameMasterTagService.getGameMasterTagsByMasterId(masterId).stream()
                .map(GameMasterTag::getTagId)
                .collect(Collectors.toList());
        
        // 2. 计算标签权重
        Map<Long, Double> tagWeights = new HashMap<>();
        for (Long tagId : masterTags) {
            double weight = calculateTagWeight(masterId, tagId);
            tagWeights.put(tagId, weight);
        }
        
        // 3. 更新游戏陪玩-标签权重矩阵
        masterTagWeights.put(masterId, tagWeights);
    }
    
    @Override
    public void updateGameFeatures(Long gameId) {
        // 1. 获取游戏的标签数据
        List<Long> gameTags = tagService.getGameTags(gameId);
        
        // 2. 计算标签权重
        Map<Long, Double> tagWeights = new HashMap<>();
        for (Long tagId : gameTags) {
            double weight = calculateTagWeight(gameId, tagId);
            tagWeights.put(tagId, weight);
        }
        
        // 3. 更新游戏-标签权重矩阵
        gameTagWeights.put(gameId, tagWeights);
    }
    
    @Override
    public void trainModel() {
        // 1. 初始化数据结构
        userTagWeights = new HashMap<>();
        masterTagWeights = new HashMap<>();
        gameTagWeights = new HashMap<>();
        
        // 2. 更新所有用户的特征向量
        for (Long userId : getAllUsers()) {
            updateUserFeatures(userId);
        }
        
        // 3. 更新所有游戏陪玩的特征向量
        for (Long masterId : getAllMasters()) {
            updateMasterFeatures(masterId);
        }
        
        // 4. 更新所有游戏的特征向量
        for (Long gameId : getAllGames()) {
            updateGameFeatures(gameId);
        }
    }
    
    @Override
    public RecommendMetrics evaluateModel() {
        // 1. 准备评估数据
        List<Long> testUsers = getTestUsers();
        List<Long> testMasters = getTestMasters();
        List<Long> testGames = getTestGames();
        
        // 2. 计算评估指标
        double precision = calculatePrecision(testUsers, testMasters);
        double recall = calculateRecall(testUsers, testMasters);
        double mrr = calculateMRR(testUsers, testMasters);
        double ndcg = calculateNDCG(testUsers, testMasters);
        double coverage = calculateCoverage(testUsers, testMasters);
        double diversity = calculateDiversity(testUsers, testMasters);
        double novelty = calculateNovelty(testUsers, testMasters);
        double timeliness = calculateTimeliness(testUsers, testMasters);
        double coldStartPerformance = calculateColdStartPerformance(testUsers, testMasters);
        
        // 3. 构建评估结果
        RecommendMetrics metrics = RecommendMetrics.builder()
                .precision(precision)
                .recall(recall)
                .mrr(mrr)
                .ndcg(ndcg)
                .coverage(coverage)
                .diversity(diversity)
                .novelty(novelty)
                .timeliness(timeliness)
                .coldStartPerformance(coldStartPerformance)
                .build();
        
        // 4. 计算F1分数
        metrics.calculateF1Score();
        
        return metrics;
    }
    
    // 辅助方法
    
    private Map<Long, Double> getUserTagWeights(Long userId) {
        if (userTagWeights.containsKey(userId)) {
            return userTagWeights.get(userId);
        }
        updateUserFeatures(userId);
        return userTagWeights.get(userId);
    }
    
    private Map<Long, Double> getMasterTagWeights(Long masterId) {
        if (masterTagWeights.containsKey(masterId)) {
            return masterTagWeights.get(masterId);
        }
        updateMasterFeatures(masterId);
        return masterTagWeights.get(masterId);
    }
    
    private Map<Long, Double> getGameTagWeights(Long gameId) {
        if (gameTagWeights.containsKey(gameId)) {
            return gameTagWeights.get(gameId);
        }
        updateGameFeatures(gameId);
        return gameTagWeights.get(gameId);
    }
    
    private List<Long> getAllUsers() {
        return userService.getUserList().stream()
                .map(user -> user.getId())
                .collect(Collectors.toList());
    }
    
    private List<Long> getAllMasters() {
        return gameMasterService.getGameMasterList().stream()
                .map(master -> master.getId())
                .collect(Collectors.toList());
    }
    
    private List<Long> getAllGames() {
        return gameService.getGameList().stream()
                .map(game -> game.getId())
                .collect(Collectors.toList());
    }
    
    private boolean isMasterGameMatch(Long masterId, Long gameId) {
        return gameMasterService.getGameMasterGames(masterId).contains(gameId);
    }
    
    /**
     * 计算标签权重
     * 
     * @param id 实体ID（用户ID、陪玩师ID或游戏ID）
     * @param tagId 标签ID
     * @return 计算后的标签权重
     */
    private double calculateTagWeight(Long id, Long tagId) {
        // 获取标签基础信息
        Tag tag = tagService.getTagById(tagId);
        if (tag == null) {
            return 0.0;
        }
        
        // 基础权重（标签自身权重）
        double baseWeight = tag.getWeight() != null ? tag.getWeight().doubleValue() : 1.0;
        
        // 根据实体类型计算权重
        if (userService.getUserById(id) != null) {
            // 用户标签权重计算
            return calculateUserTagWeight(id, tagId, baseWeight);
        } else if (gameMasterService.getGameMasterById(id) != null) {
            // 陪玩师标签权重计算
            return calculateMasterTagWeight(id, tagId, baseWeight);
        } else if (gameService.getGameById(id) != null) {
            // 游戏标签权重计算
            return calculateGameTagWeight(id, tagId, baseWeight);
        }
        
        return baseWeight;
    }
    
    /**
     * 计算用户标签权重
     */
    private double calculateUserTagWeight(Long userId, Long tagId, double baseWeight) {
        // 1. 获取用户标签关联信息
        UserTag userTag = userTagService.getUserTagByUserIdAndTagId(userId, tagId);
        if (userTag == null) {
            return 0.0;
        }
        
        // 2. 标签来源权重
        double sourceWeight = getSourceWeight(userTag.getSource());
        
        // 3. 时间衰减因子
        double timeDecay = calculateTimeDecay(userTag.getUpdateTime());
        
        // 4. 用户行为权重
        double behaviorWeight = calculateUserBehaviorWeight(userId, tagId);
        
        // 5. TF-IDF因子（标签稀有度）
        double tfIdfFactor = calculateTagTfIdf(tagId);
        
        // 6. 用户标签权重（如果有）
        double userDefinedWeight = userTag.getWeight() != null ? userTag.getWeight().doubleValue() : 1.0;
        
        // 7. 综合计算最终权重
        return baseWeight * sourceWeight * timeDecay * (1 + behaviorWeight) * tfIdfFactor * userDefinedWeight;
    }
    
    /**
     * 计算陪玩师标签权重
     */
    private double calculateMasterTagWeight(Long masterId, Long tagId, double baseWeight) {
        // 1. 获取陪玩师标签关联信息
        GameMasterTag masterTag = gameMasterTagService.getGameMasterTagByMasterIdAndTagId(masterId, tagId);
        if (masterTag == null) {
            return 0.0;
        }
        
        // 2. 标签来源权重
        double sourceWeight = getSourceWeight(masterTag.getSource());
        
        // 3. 时间衰减因子
        double timeDecay = calculateTimeDecay(masterTag.getUpdateTime());
        
        // 4. 陪玩师接单数据权重
        double orderWeight = calculateMasterOrderWeight(masterId, tagId);
        
        // 5. TF-IDF因子（标签稀有度）
        double tfIdfFactor = calculateTagTfIdf(tagId);
        
        // 6. 陪玩师标签权重（如果有）
        double masterDefinedWeight = masterTag.getWeight() != null ? masterTag.getWeight().doubleValue() : 1.0;
        
        // 7. 综合计算最终权重
        return baseWeight * sourceWeight * timeDecay * (1 + orderWeight) * tfIdfFactor * masterDefinedWeight;
    }
    
    /**
     * 计算游戏标签权重
     */
    private double calculateGameTagWeight(Long gameId, Long tagId, double baseWeight) {
        // 1. 游戏标签通常是固定的，权重主要由标签本身和游戏的热度决定
        
        // 2. 游戏热度因子（基于游戏的订单量）
        double popularityFactor = calculateGamePopularity(gameId);
        
        // 3. TF-IDF因子（标签稀有度）
        double tfIdfFactor = calculateTagTfIdf(tagId);
        
        // 4. 综合计算最终权重
        return baseWeight * popularityFactor * tfIdfFactor;
    }
    
    /**
     * 根据标签来源计算权重系数
     * 1-系统计算，2-用户/陪玩师选择，3-运营设置
     */
    private double getSourceWeight(Integer source) {
        if (source == null) {
            return 1.0;
        }
        
        switch (source) {
            case 1: // 系统计算
                return 0.8;
            case 2: // 用户/陪玩师选择
                return 1.2;
            case 3: // 运营设置
                return 1.5;
            default:
                return 1.0;
        }
    }
    
    /**
     * 计算时间衰减因子
     * 随着时间推移，标签的权重会逐渐降低
     */
    private double calculateTimeDecay(Date updateTime) {
        if (updateTime == null) {
            return 0.5; // 默认值
        }
        
        // 计算时间差（天）
        long diffInMillies = System.currentTimeMillis() - updateTime.getTime();
        double diffInDays = diffInMillies / (24.0 * 60 * 60 * 1000);
        
        // 使用指数衰减函数，半衰期为30天
        return Math.exp(-Math.log(2) * diffInDays / 30.0);
    }
    
    /**
     * 计算用户行为权重
     * 根据用户的行为类型（浏览、收藏、下单等）计算权重
     */
    private double calculateUserBehaviorWeight(Long userId, Long tagId) {
        // 获取用户行为数据
        List<UserBehavior> behaviors = userBehaviorService.getUserBehaviorsByUserId(userId);
        
        double totalWeight = 0.0;
        
        for (UserBehavior behavior : behaviors) {
            // 获取行为目标（陪玩师或游戏）的标签
            List<Long> targetTags = new ArrayList<>();
            
            if ("1".equals(behavior.getTargetType())) { // 陪玩师
                targetTags = gameMasterTagService.getGameMasterTagsByMasterId(behavior.getTargetId())
                        .stream()
                        .map(GameMasterTag::getTagId)
                        .collect(Collectors.toList());
            } else if ("2".equals(behavior.getTargetType())) { // 游戏
                targetTags = tagService.getGameTags(behavior.getTargetId());
            }
            
            // 如果目标包含当前标签
            if (targetTags.contains(tagId)) {
                // 根据行为类型赋予不同权重
                switch (behavior.getType()) {
                    case "1": // 浏览
                        totalWeight += 0.2;
                        break;
                    case "2": // 收藏
                        totalWeight += 0.5;
                        break;
                    case "3": // 下单
                        totalWeight += 1.0;
                        break;
                    default:
                        totalWeight += 0.1;
                }
                
                // 考虑时间衰减
                totalWeight *= calculateTimeDecay(behavior.getCreateTime());
            }
        }
        
        return totalWeight;
    }
    
    /**
     * 计算陪玩师接单数据权重
     */
    private double calculateMasterOrderWeight(Long masterId, Long tagId) {
        // 获取陪玩师的订单数据
        List<Order> orders = orderService.getOrdersByMasterId(masterId);
        
        // 计算与标签相关的订单比例
        long totalOrders = orders.size();
        if (totalOrders == 0) {
            return 0.0;
        }
        
        long tagRelatedOrders = orders.stream()
                .filter(order -> {
                    // 获取订单关联的游戏标签
                    List<Long> gameTags = tagService.getGameTags(order.getGameId());
                    return gameTags.contains(tagId);
                })
                .count();
        
        return (double) tagRelatedOrders / totalOrders;
    }
    
    /**
     * 计算游戏热度因子
     */
    private double calculateGamePopularity(Long gameId) {
        // 获取游戏的订单数量
        List<Order> gameOrders = orderService.getOrdersByGameId(gameId);
        
        // 获取所有游戏的平均订单数量
        double avgOrderCount = getAllGames().stream()
                .mapToLong(id -> orderService.getOrdersByGameId(id).size())
                .average()
                .orElse(1.0);
        
        // 计算热度因子（游戏订单数 / 平均订单数）
        double popularityFactor = gameOrders.size() / avgOrderCount;
        
        // 使用对数函数平滑热度差异
        return 0.5 + 0.5 * Math.log(1 + popularityFactor);
    }
    
    /**
     * 计算标签的TF-IDF因子
     * TF-IDF用于衡量标签在系统中的重要性，稀有标签具有更高的权重
     */
    private double calculateTagTfIdf(Long tagId) {
        // 获取标签在系统中的使用频率
        int tagFrequency = tagService.getTagUsageCount(tagId);
        
        // 获取系统中的总标签数
        int totalTags = tagService.getAllTags().size();
        
        if (tagFrequency == 0 || totalTags == 0) {
            return 1.0;
        }
        
        // 计算逆文档频率（IDF）
        double idf = Math.log((double) totalTags / tagFrequency);
        
        // 归一化到合理范围
        return 0.5 + 0.5 * idf / Math.log(totalTags);
    }
    
    private double calculateTagWeightSimilarity(Map<Long, Double> weights1, Map<Long, Double> weights2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        // 计算点积和范数
        for (Long tagId : weights1.keySet()) {
            if (weights2.containsKey(tagId)) {
                dotProduct += weights1.get(tagId) * weights2.get(tagId);
            }
            norm1 += weights1.get(tagId) * weights1.get(tagId);
        }
        
        for (Double weight : weights2.values()) {
            norm2 += weight * weight;
        }
        
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (sqrt(norm1) * sqrt(norm2));
    }
    
    private List<Long> getTestUsers() {
        // 1. 获取所有用户
        List<Long> allUsers = getAllUsers();
        
        // 2. 随机选择20%的用户作为测试集
        int testSize = (int) (allUsers.size() * 0.2);
        Collections.shuffle(allUsers);
        
        return allUsers.subList(0, min(testSize, allUsers.size()));
    }
    
    private List<Long> getTestMasters() {
        // 1. 获取所有游戏陪玩
        List<Long> allMasters = getAllMasters();
        
        // 2. 随机选择20%的游戏陪玩作为测试集
        int testSize = (int) (allMasters.size() * 0.2);
        Collections.shuffle(allMasters);
        
        return allMasters.subList(0, min(testSize, allMasters.size()));
    }
    
    private List<Long> getTestGames() {
        // 1. 获取所有游戏
        List<Long> allGames = getAllGames();
        
        // 2. 随机选择20%的游戏作为测试集
        int testSize = (int) (allGames.size() * 0.2);
        Collections.shuffle(allGames);
        
        return allGames.subList(0, min(testSize, allGames.size()));
    }
    
    private double calculatePrecision(List<Long> testUsers, List<Long> testMasters) {
        double totalPrecision = 0.0;
        int userCount = 0;
        
        for (Long userId : testUsers) {
            // 1. 获取推荐结果
            List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
            if (recommendations.isEmpty()) {
                continue;
            }
            
            // 2. 获取用户实际交互的游戏陪玩
            List<Long> actualMasters = userBehaviorService.getUserBehaviorsByUserId(userId).stream()
                    .filter(behavior -> "3".equals(behavior.getType())) // 下单行为
                    .map(UserBehavior::getTargetId)
                    .collect(Collectors.toList());
            
            // 3. 计算准确率
            long correctPredictions = recommendations.stream()
                    .map(GameMaster::getId)
                    .filter(actualMasters::contains)
                    .count();
            
            totalPrecision += (double) correctPredictions / recommendations.size();
            userCount++;
        }
        
        return userCount > 0 ? totalPrecision / userCount : 0.0;
    }
    
    private double calculateRecall(List<Long> testUsers, List<Long> testMasters) {
        double totalRecall = 0.0;
        int userCount = 0;
        
        for (Long userId : testUsers) {
            // 1. 获取推荐结果
            List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
            
            // 2. 获取用户实际交互的游戏陪玩
            List<Long> actualMasters = userBehaviorService.getUserBehaviorsByUserId(userId).stream()
                    .filter(behavior -> Objects.equals(behavior.getType(), "3")) // 下单行为
                    .map(UserBehavior::getTargetId)
                    .collect(Collectors.toList());
            
            if (actualMasters.isEmpty()) {
                continue;
            }
            
            // 3. 计算召回率
            long correctPredictions = recommendations.stream()
                    .map(GameMaster::getId)
                    .filter(actualMasters::contains)
                    .count();
            
            totalRecall += (double) correctPredictions / actualMasters.size();
            userCount++;
        }
        
        return userCount > 0 ? totalRecall / userCount : 0.0;
    }
    
    private double calculateMRR(List<Long> testUsers, List<Long> testMasters) {
        double totalMRR = 0.0;
        int userCount = 0;
        
        for (Long userId : testUsers) {
            // 1. 获取推荐结果
            List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
            
            // 2. 获取用户实际交互的游戏陪玩
            List<Long> actualMasters = userBehaviorService.getUserBehaviorsByUserId(userId).stream()
                    .filter(behavior -> behavior.getType() == "3") // 下单行为
                    .map(UserBehavior::getTargetId)
                    .collect(Collectors.toList());
            
            if (actualMasters.isEmpty()) {
                continue;
            }
            
            // 3. 计算第一个正确推荐的位置
            for (int i = 0; i < recommendations.size(); i++) {
                if (actualMasters.contains(recommendations.get(i).getId())) {
                    totalMRR += 1.0 / (i + 1);
                    break;
                }
            }
            
            userCount++;
        }
        
        return userCount > 0 ? totalMRR / userCount : 0.0;
    }
    
    private double calculateNDCG(List<Long> testUsers, List<Long> testMasters) {
        double totalNDCG = 0.0;
        int userCount = 0;
        
        for (Long userId : testUsers) {
            // 1. 获取推荐结果
            List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
            
            // 2. 获取用户实际交互的游戏陪玩及其评分
            Map<Long, Double> actualRatings = userBehaviorService.getUserBehaviorsByUserId(userId).stream()
                    .filter(behavior -> Objects.equals(behavior.getType(), "3")) // 下单行为
                    .collect(Collectors.toMap(
                            UserBehavior::getTargetId,
                            behavior -> 1.0 // 简化处理，实际应该使用真实评分
                    ));
            
            if (actualRatings.isEmpty()) {
                continue;
            }
            
            // 3. 计算DCG
            double dcg = 0.0;
            for (int i = 0; i < recommendations.size(); i++) {
                Long masterId = recommendations.get(i).getId();
                if (actualRatings.containsKey(masterId)) {
                    dcg += actualRatings.get(masterId) / MathUtils.log2(i + 2);
                }
            }
            
            // 4. 计算IDCG（理想情况下的DCG）
            double idcg = 0.0;
            List<Double> idealRatings = new ArrayList<>(actualRatings.values());
            Collections.sort(idealRatings, Collections.reverseOrder());
            for (int i = 0; i < min(idealRatings.size(), recommendations.size()); i++) {
                idcg += idealRatings.get(i) / MathUtils.log2(i + 2);
            }
            
            // 5. 计算NDCG
            totalNDCG += idcg > 0 ? dcg / idcg : 0.0;
            userCount++;
        }
        
        return userCount > 0 ? totalNDCG / userCount : 0.0;
    }
    
    private double calculateCoverage(List<Long> testUsers, List<Long> testMasters) {
        // 1. 获取所有可能的游戏陪玩
        Set<Long> allMasters = new HashSet<>(getAllMasters());
        
        // 2. 获取所有被推荐的游戏陪玩
        Set<Long> recommendedMasters = new HashSet<>();
        for (Long userId : testUsers) {
            List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
            recommendations.forEach(master -> recommendedMasters.add(master.getId()));
        }
        
        // 3. 计算覆盖率
        return allMasters.isEmpty() ? 0.0 : (double) recommendedMasters.size() / allMasters.size();
    }
    
    private double calculateDiversity(List<Long> testUsers, List<Long> testMasters) {
        double totalDiversity = 0.0;
        int userCount = 0;
        
        for (Long userId : testUsers) {
            // 1. 获取推荐结果
            List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
            if (recommendations.size() < 2) {
                continue;
            }
            
            // 2. 计算推荐结果之间的平均相似度
            double totalSimilarity = 0.0;
            int pairCount = 0;
            
            for (int i = 0; i < recommendations.size(); i++) {
                for (int j = i + 1; j < recommendations.size(); j++) {
                    double similarity = calculateMasterSimilarity(
                            recommendations.get(i).getId(),
                            recommendations.get(j).getId()
                    );
                    totalSimilarity += similarity;
                    pairCount++;
                }
            }
            
            // 3. 计算多样性（1 - 平均相似度）
            totalDiversity += pairCount > 0 ? 1.0 - (totalSimilarity / pairCount) : 0.0;
            userCount++;
        }
        
        return userCount > 0 ? totalDiversity / userCount : 0.0;
    }
    
    private double calculateNovelty(List<Long> testUsers, List<Long> testMasters) {
        double totalNovelty = 0.0;
        int userCount = 0;
        
        for (Long userId : testUsers) {
            // 1. 获取推荐结果
            List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
            
            // 2. 获取用户历史交互的游戏陪玩
            Set<Long> historicalMasters = userBehaviorService.getUserBehaviorsByUserId(userId).stream()
                    .filter(behavior -> Objects.equals(behavior.getType(), "3")) // 下单行为
                    .map(UserBehavior::getTargetId)
                    .collect(Collectors.toSet());
            
            // 3. 计算新颖性（推荐结果中未交互过的比例）
            long newMasters = recommendations.stream()
                    .map(GameMaster::getId)
                    .filter(masterId -> !historicalMasters.contains(masterId))
                    .count();
            
            totalNovelty += (double) newMasters / recommendations.size();
            userCount++;
        }
        
        return userCount > 0 ? totalNovelty / userCount : 0.0;
    }
    
    private double calculateTimeliness(List<Long> testUsers, List<Long> testMasters) {
        double totalTimeliness = 0.0;
        int userCount = 0;
        
        for (Long userId : testUsers) {
            // 1. 获取推荐结果
            List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
            
            // 2. 计算每个推荐结果的时效性得分
            double userTimeliness = 0.0;
            for (GameMaster master : recommendations) {
                // 获取游戏陪玩最近的活动时间
                Date lastActiveTime = getLastActiveTime(master.getId());
                if (lastActiveTime != null) {
                    long timeDiff = System.currentTimeMillis() - lastActiveTime.getTime();
                    double daysDiff = timeDiff / (24.0 * 60 * 60 * 1000);
                    userTimeliness += exp(-daysDiff / 7.0); // 7天为半衰期
                }
            }
            
            totalTimeliness += recommendations.isEmpty() ? 0.0 : userTimeliness / recommendations.size();
            userCount++;
        }
        
        return userCount > 0 ? totalTimeliness / userCount : 0.0;
    }
    
    private double calculateColdStartPerformance(List<Long> testUsers, List<Long> testMasters) {
        double totalPerformance = 0.0;
        int userCount = 0;
        
        for (Long userId : testUsers) {
            // 1. 获取用户的行为数量
            int behaviorCount = userBehaviorService.getUserBehaviorsByUserId(userId).size();
            
            // 2. 如果是冷启动用户（行为数量少于阈值）
            if (behaviorCount < 5) {
                // 获取推荐结果
                List<GameMaster> recommendations = recommendGameMasters(userId, null, 10);
                
                // 获取用户实际交互的游戏陪玩
                List<Long> actualMasters = userBehaviorService.getUserBehaviorsByUserId(userId).stream()
                        .filter(behavior -> behavior.getType() == "3") // 下单行为
                        .map(UserBehavior::getTargetId)
                        .collect(Collectors.toList());
                
                if (!actualMasters.isEmpty()) {
                    // 计算准确率
                    long correctPredictions = recommendations.stream()
                            .map(GameMaster::getId)
                            .filter(actualMasters::contains)
                            .count();
                    
                    totalPerformance += (double) correctPredictions / recommendations.size();
                    userCount++;
                }
            }
        }
        
        return userCount > 0 ? totalPerformance / userCount : 0.0;
    }
    
    private Date getLastActiveTime(Long masterId) {
        // 获取游戏陪玩最近的活动时间（可以是最近一次接单时间）
        return orderService.getOrdersByMasterId(masterId).stream()
                .map(Order::getCreateTime)
                .max(Date::compareTo)
                .orElse(null);
    }
} 