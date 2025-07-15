package com.recommend.service.impl;

import com.recommend.service.GameMasterService;
import com.recommend.service.mapper.GameMasterMapper;
import com.recommend.common.entity.GameMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
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
        gameMaster.setScore(score);
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
        // TODO: 实现获取陪玩标签的逻辑
        return new ArrayList<>();
    }
    
    @Override
    public List<String> getGameMasterGames(Long masterId) {
        // TODO: 实现获取陪玩游戏的逻辑
        return new ArrayList<>();
    }
    
    @Override
    public List<String> getGameMasterOrders(Long masterId) {
        // TODO: 实现获取陪玩订单的逻辑
        return new ArrayList<>();
    }
    
    @Override
    public List<GameMaster> recommendGameMasters(Long userId, Long gameId, Integer limit) {
        // TODO: 实现陪玩推荐的逻辑
        return new ArrayList<>();
    }
} 