package com.recommend.service.impl;

import com.recommend.service.GameMasterService;
import com.recommend.service.mapper.GameMasterMapper;
import com.recommend.common.entity.GameMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
public class GameMasterServiceImpl implements GameMasterService {
    
    @Autowired
    private GameMasterMapper gameMasterMapper;
    
    @Override
    public GameMaster getGameMasterById(Long masterId) {
        return gameMasterMapper.selectById(masterId);
    }
    
    @Override
    public List<GameMaster> getGameMasterList() {
        return gameMasterMapper.selectList(null);
    }
    
    @Override
    public List<GameMaster> searchGameMasters(String keyword, Integer status) {
        return gameMasterMapper.searchGameMasters(keyword, status);
    }
    
    @Override
    public List<GameMaster> getGameMastersByRegion(String region) {
        return gameMasterMapper.selectByRegion(region);
    }
    
    @Override
    public List<GameMaster> getGameMastersByStatus(Integer status) {
        return gameMasterMapper.selectByStatus(status);
    }
    
    @Override
    public List<GameMaster> getGameMastersByScoreRange(Double minScore, Double maxScore) {
        return gameMasterMapper.selectByScoreRange(minScore, maxScore);
    }
    
    @Override
    public List<GameMaster> getGameMastersByPriceRange(Double minPrice, Double maxPrice) {
        return gameMasterMapper.selectByPriceRange(minPrice, maxPrice);
    }
    
    @Override
    @Transactional
    public void createGameMaster(GameMaster gameMaster) {
        gameMasterMapper.insert(gameMaster);
    }
    
    @Override
    @Transactional
    public void updateGameMaster(GameMaster gameMaster) {
        gameMasterMapper.updateById(gameMaster);
    }
    
    @Override
    @Transactional
    public void updateGameMasterStatus(Long masterId, Integer status) {
        GameMaster gameMaster = new GameMaster();
        gameMaster.setId(masterId);
        gameMaster.setStatus(status);
        gameMasterMapper.updateById(gameMaster);
    }
    
    @Override
    @Transactional
    public void updateGameMasterScore(Long masterId, Double score) {
        GameMaster gameMaster = new GameMaster();
        gameMaster.setId(masterId);
        gameMaster.setScore(new BigDecimal(score));
        gameMasterMapper.updateById(gameMaster);
    }
    
    @Override
    @Transactional
    public void deleteGameMaster(Long masterId) {
        gameMasterMapper.deleteById(masterId);
    }
    
    @Override
    public GameMaster getGameMasterProfile(Long masterId) {
        return gameMasterMapper.selectById(masterId);
    }
    
    @Override
    public List<String> getGameMasterTags(Long masterId) {
        try {
            GameMaster master = getGameMasterById(masterId);
            if (master != null && master.getTags() != null) {
                return Arrays.asList(master.getTags().split(","));
            }
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("获取陪玩师标签失败，陪玩师ID: {}", masterId, e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<String> getGameMasterGames(Long masterId) {
        try {
            GameMaster master = getGameMasterById(masterId);
            if (master != null && master.getGameTypes() != null) {
                return Arrays.asList(master.getGameTypes().split(","));
            }
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("获取陪玩师游戏列表失败，陪玩师ID: {}", masterId, e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<String> getGameMasterOrders(Long masterId) {
        try {
            // 这里应该查询订单表，暂时返回基于订单数的模拟数据
            GameMaster master = getGameMasterById(masterId);
            List<String> orders = new ArrayList<>();
            if (master != null && master.getOrderCount() != null) {
                // 模拟返回最近的订单ID
                for (int i = 0; i < Math.min(10, master.getOrderCount()); i++) {
                    orders.add("ORDER_" + masterId + "_" + (System.currentTimeMillis() - i * 86400000));
                }
            }
            return orders;
        } catch (Exception e) {
            log.error("获取陪玩师订单列表失败，陪玩师ID: {}", masterId, e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<GameMaster> recommendGameMasters(Long userId, Long gameId, Integer limit) {
        try {
            // 基础推荐逻辑：返回评分高、在线的陪玩师
            List<GameMaster> allMasters = gameMasterMapper.selectByStatus(1); // 在线状态
            
            return allMasters.stream()
                .filter(master -> master.getScore() != null && master.getScore().doubleValue() >= 4.0)
                .sorted((m1, m2) -> {
                    // 按评分和订单数综合排序
                    double score1 = m1.getScore().doubleValue() + (m1.getOrderCount() != null ? m1.getOrderCount() / 100.0 : 0);
                    double score2 = m2.getScore().doubleValue() + (m2.getOrderCount() != null ? m2.getOrderCount() / 100.0 : 0);
                    return Double.compare(score2, score1);
                })
                .limit(limit != null ? limit : 10)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            log.error("推荐陪玩师失败，用户ID: {}, 游戏ID: {}", userId, gameId, e);
            return new ArrayList<>();
        }
    }
} 