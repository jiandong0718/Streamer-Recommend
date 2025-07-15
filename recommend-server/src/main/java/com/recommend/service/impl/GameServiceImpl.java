package com.recommend.service.impl;

import com.recommend.service.GameService;
import com.recommend.service.mapper.GameMapper;
import com.recommend.common.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
public class GameServiceImpl implements GameService {
    
    @Autowired
    private GameMapper gameMapper;
    
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
        // TODO: 实现获取游戏陪玩师列表的逻辑
        return new ArrayList<>();
    }
    
    @Override
    public List<String> getGameOrders(Long gameId) {
        // TODO: 实现获取游戏订单列表的逻辑
        return new ArrayList<>();
    }
    
    @Override
    public List<Game> recommendGames(Long userId, Integer limit) {
        // TODO: 实现游戏推荐逻辑
        return new ArrayList<>();
    }
} 