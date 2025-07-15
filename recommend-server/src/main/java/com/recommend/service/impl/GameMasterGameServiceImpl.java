package com.recommend.service.impl;

import com.recommend.common.entity.GameMasterGame;
import com.recommend.service.GameMasterGameService;
import com.recommend.service.mapper.GameMasterGameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class GameMasterGameServiceImpl implements GameMasterGameService {

    @Autowired
    private GameMasterGameMapper gameMasterGameMapper;

    @Override
    public List<GameMasterGame> getGameMasterGamesByMasterId(Long masterId) {
        return gameMasterGameMapper.selectByMasterId(masterId);
    }

    @Override
    public List<GameMasterGame> getGameMasterGamesByGameId(Long gameId) {
        return gameMasterGameMapper.selectByGameId(gameId);
    }

    @Override
    public List<GameMasterGame> getGameMasterGamesByScoreRange(Double minScore, Double maxScore) {
        return Collections.emptyList();
    }

    @Override
    public List<GameMasterGame> getGameMasterGamesByOrderCountRange(Integer minOrderCount, Integer maxOrderCount) {
        return Collections.emptyList();
    }

    public List<GameMasterGame> getGameMasterGamesByWeightRange(Double minWeight, Double maxWeight) {
        return gameMasterGameMapper.selectByWeightRange(minWeight, maxWeight);
    }

    @Override
    @Transactional
    public void addGameMasterGame(GameMasterGame gameMasterGame) {
        gameMasterGameMapper.insert(gameMasterGame);
    }

    @Override
    public void updateGameMasterGameScore(Long gameId, Double score) {

    }

    @Override
    public void updateGameMasterGameOrderCount(Long gameId, Integer orderCount) {

    }

    @Override
    public void deleteGameMasterGame(Long gameId) {

    }

    @Override
    public void batchAddGameMasterGames(List<GameMasterGame> gameMasterGames) {

    }

    @Override
    public void batchDeleteGameMasterGames(List<Long> gameIds) {

    }

    @Override
    public GameMasterGame getGameMasterGameById(Long id) {
        return null;
    }

    public void updateGameMasterGame(GameMasterGame gameMasterGame) {

    }

    @Transactional
    public void updateGameMasterGameWeight(Long masterId, Long gameId, Double weight) {
        gameMasterGameMapper.updateWeight(masterId, gameId, weight);
    }
}