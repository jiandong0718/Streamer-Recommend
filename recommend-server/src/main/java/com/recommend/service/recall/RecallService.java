package com.recommend.service.recall;

import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.UserProfile;
import com.recommend.service.UserProfileService;
import com.recommend.service.mapper.GameMasterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecallService {
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Autowired
    private GameMasterMapper gameMasterMapper;
    
    public List<GameMaster> recallCandidates(Long userId) {
        // 1. 获取用户画像
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        
        // 2. 多路召回

        // 2.1 基于标签的召回
        List<GameMaster> tagBasedCandidates = recallByTags(userProfile);
        List<GameMaster> candidates = new ArrayList<>(tagBasedCandidates);
        
        // 2.2 基于相似用户的召回
        List<GameMaster> similarUserCandidates = recallBySimilarUsers(userId);
        candidates.addAll(similarUserCandidates);
        
        // 2.3 基于热门度的召回
        List<GameMaster> popularCandidates = recallByPopularity();
        candidates.addAll(popularCandidates);
        
        // 3. 去重
        return candidates.stream()
            .distinct()
            .collect(Collectors.toList());
    }
    
    private List<GameMaster> recallByTags(UserProfile userProfile) {
        // 从用户画像中获取游戏类型偏好
        String gameTypes = userProfile.getGameTypes();
        if (gameTypes == null || gameTypes.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 将游戏类型字符串转换为标签列表
        List<String> tags = Arrays.asList(gameTypes.split(","));
        return gameMasterMapper.findByTags(tags);
    }
    
    private List<GameMaster> recallBySimilarUsers(Long userId) {
        try {
            // 获取用户画像
            UserProfile userProfile = userProfileService.getUserProfile(userId);
            if (userProfile == null) {
                return new ArrayList<>();
            }
            
            // 基于用户标签找相似用户
            List<UserProfile> similarUsers = findSimilarUsers(userProfile);
            
            // 获取相似用户喜欢的陪玩师
            Set<Long> masterIds = new HashSet<>();
            for (UserProfile similarUser : similarUsers) {
                // 这里应该查询用户的历史订单或行为数据
                // 暂时基于用户偏好的游戏类型来推荐
                if (similarUser.getGameTypes() != null) {
                    List<GameMaster> masters = gameMasterMapper.selectByGameTypes(similarUser.getGameTypes());
                    masters.stream()
                        .filter(master -> master.getStatus() == 1) // 在线状态
                        .limit(5) // 每个相似用户贡献最多5个陪玩师
                        .forEach(master -> masterIds.add(master.getId()));
                }
            }
            
            // 根据ID批量获取陪玩师信息
            return masterIds.stream()
                .map(gameMasterMapper::selectById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("基于相似用户召回失败，用户ID: {}", userId, e);
            return new ArrayList<>();
        }
    }
    
    private List<GameMaster> recallByPopularity() {
        try {
            // 获取热门陪玩师（基于评分和订单数）
            return gameMasterMapper.selectByStatus(1).stream()
                .filter(master -> master.getScore() != null && master.getOrderCount() != null)
                .sorted((m1, m2) -> {
                    // 综合评分和订单数排序
                    double score1 = m1.getScore().doubleValue() * 0.6 + 
                                   Math.min(1.0, m1.getOrderCount() / 100.0) * 0.4;
                    double score2 = m2.getScore().doubleValue() * 0.6 + 
                                   Math.min(1.0, m2.getOrderCount() / 100.0) * 0.4;
                    return Double.compare(score2, score1);
                })
                .limit(50) // 取前50个热门陪玩师
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("基于热门度召回失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 找到与目标用户相似的用户
     */
    private List<UserProfile> findSimilarUsers(UserProfile targetUser) {
        try {
            // 获取所有用户画像（实际应用中需要优化，比如只获取活跃用户）
            List<UserProfile> allUsers = userProfileService.getAllUserProfiles();
            
            return allUsers.stream()
                .filter(user -> !user.getUserId().equals(targetUser.getUserId())) // 排除自己
                .filter(user -> calculateUserSimilarity(targetUser, user) > 0.3) // 相似度阈值
                .sorted((u1, u2) -> {
                    double sim1 = calculateUserSimilarity(targetUser, u1);
                    double sim2 = calculateUserSimilarity(targetUser, u2);
                    return Double.compare(sim2, sim1);
                })
                .limit(10) // 取前10个最相似的用户
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("查找相似用户失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 计算两个用户的相似度
     */
    private double calculateUserSimilarity(UserProfile user1, UserProfile user2) {
        double similarity = 0.0;
        
        // 地区相似度
        if (user1.getRegion() != null && user2.getRegion() != null) {
            if (user1.getRegion().equals(user2.getRegion())) {
                similarity += 0.2;
            }
        }
        
        // 年龄相似度
        if (user1.getAge() != null && user2.getAge() != null) {
            int ageDiff = Math.abs(user1.getAge() - user2.getAge());
            similarity += Math.max(0, 0.2 - ageDiff / 20.0);
        }
        
        // 游戏类型相似度
        if (user1.getGameTypes() != null && user2.getGameTypes() != null) {
            List<String> gameTypes1 = Arrays.asList(user1.getGameTypes().split(","));
            List<String> gameTypes2 = Arrays.asList(user2.getGameTypes().split(","));
            
            long commonGames = gameTypes1.stream()
                .filter(gameTypes2::contains)
                .count();
            
            if (!gameTypes1.isEmpty() && !gameTypes2.isEmpty()) {
                similarity += (double) commonGames / Math.max(gameTypes1.size(), gameTypes2.size()) * 0.4;
            }
        }
        
        // 标签相似度
        if (user1.getTags() != null && user2.getTags() != null) {
            List<String> tags1 = Arrays.asList(user1.getTags().split(","));
            List<String> tags2 = Arrays.asList(user2.getTags().split(","));
            
            long commonTags = tags1.stream()
                .filter(tags2::contains)
                .count();
            
            if (!tags1.isEmpty() && !tags2.isEmpty()) {
                similarity += (double) commonTags / Math.max(tags1.size(), tags2.size()) * 0.2;
            }
        }
        
        return Math.min(1.0, similarity);
    }
} 