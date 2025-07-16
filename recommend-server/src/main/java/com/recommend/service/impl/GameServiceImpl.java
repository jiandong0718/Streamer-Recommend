package com.recommend.service.impl;

import com.recommend.service.GameService;
import com.recommend.service.UserProfileService;
import com.recommend.service.mapper.GameMapper;
import com.recommend.service.mapper.GameMasterMapper;
import com.recommend.common.entity.Game;
import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
public class GameServiceImpl implements GameService {
    
    @Autowired
    private GameMapper gameMapper;
    
    @Autowired
    private GameMasterMapper gameMasterMapper;
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Override
    public Game getGameById(Long gameId) {
        return gameMapper.selectById(gameId);
    }
    
    @Override
    public List<Game> getGameList() {
        return gameMapper.selectList(null);
    }
    
    @Override
    public List<Game> searchGames(String keyword, Integer status) {
        return gameMapper.searchGames(keyword, status);
    }
    
    @Override
    @Transactional
    public void createGame(Game game) {
        gameMapper.insert(game);
    }
    
    @Override
    @Transactional
    public void updateGame(Game game) {
        gameMapper.updateById(game);
    }
    
    @Override
    @Transactional
    public void deleteGame(Long gameId) {
        gameMapper.deleteById(gameId);
    }
    
    @Override
    public List<String> getGameMasters(Long gameId) {
        try {
            // 查询该游戏的陪玩师列表
            List<GameMaster> masters = gameMasterMapper.selectByGameId(gameId);
            return masters.stream()
                .filter(master -> master.getStatus() == 1) // 只返回在线的陪玩师
                .map(master -> master.getId().toString())
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            log.error("获取游戏陪玩师列表失败，游戏ID: {}", gameId, e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<String> getGameOrders(Long gameId) {
        try {
            // 这里应该查询订单表，暂时返回基于游戏的模拟订单数据
            Game game = getGameById(gameId);
            List<String> orders = new ArrayList<>();
            if (game != null) {
                // 模拟返回该游戏的最近订单ID
                for (int i = 0; i < 20; i++) {
                    orders.add("GAME_ORDER_" + gameId + "_" + (System.currentTimeMillis() - i * 3600000));
                }
            }
            return orders;
        } catch (Exception e) {
            log.error("获取游戏订单列表失败，游戏ID: {}", gameId, e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Game> recommendGames(Long userId, Integer limit) {
        try {
            // 获取用户画像
            UserProfile userProfile = userProfileService.getUserProfile(userId);
            if (userProfile == null) {
                // 新用户返回热门游戏
                return getHotGames(limit);
            }
            
            List<Game> allGames = gameMapper.selectByStatus(1); // 获取启用的游戏
            
            // 基于用户偏好进行游戏推荐
            return allGames.stream()
                .filter(game -> isGameSuitableForUser(game, userProfile))
                .sorted((g1, g2) -> {
                    // 按游戏与用户的匹配度排序
                    double score1 = calculateGameUserMatchScore(g1, userProfile);
                    double score2 = calculateGameUserMatchScore(g2, userProfile);
                    return Double.compare(score2, score1);
                })
                .limit(limit != null ? limit : 10)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            log.error("推荐游戏失败，用户ID: {}", userId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取热门游戏
     */
    private List<Game> getHotGames(Integer limit) {
        try {
            return gameMapper.selectByStatus(1).stream()
                .sorted((g1, g2) -> {
                    // 按热度排序（这里简化为按ID倒序，实际应该有热度字段）
                    return Long.compare(g2.getId(), g1.getId());
                })
                .limit(limit != null ? limit : 10)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            log.error("获取热门游戏失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 判断游戏是否适合用户
     */
    private boolean isGameSuitableForUser(Game game, UserProfile userProfile) {
        // 基于用户的游戏类型偏好
        if (userProfile.getGameTypes() != null && game.getType() != null) {
            List<String> userGameTypes = Arrays.asList(userProfile.getGameTypes().split(","));
            return userGameTypes.contains(game.getType());
        }
        return true; // 默认适合
    }
    
    /**
     * 计算游戏与用户的匹配得分
     */
    private double calculateGameUserMatchScore(Game game, UserProfile userProfile) {
        double score = 0.0;
        
        // 游戏类型匹配度
        if (userProfile.getGameTypes() != null && game.getType() != null) {
            List<String> userGameTypes = Arrays.asList(userProfile.getGameTypes().split(","));
            if (userGameTypes.contains(game.getType())) {
                score += 0.5;
            }
        }
        
        // 游戏标签匹配度
        if (userProfile.getTags() != null && game.getTags() != null) {
            List<String> userTags = Arrays.asList(userProfile.getTags().split(","));
            List<String> gameTags = Arrays.asList(game.getTags().split(","));
            
            long matchCount = userTags.stream()
                .filter(gameTags::contains)
                .count();
            
            if (!userTags.isEmpty()) {
                score += (double) matchCount / userTags.size() * 0.3;
            }
        }
        
        // 游戏热度加分（基于ID，实际应该有专门的热度字段）
        score += Math.min(0.2, game.getId() / 1000.0);
        
        return score;
    }
} 