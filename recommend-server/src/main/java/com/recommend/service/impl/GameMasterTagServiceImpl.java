package com.recommend.service.impl;

import com.recommend.service.GameMasterTagService;
import com.recommend.service.mapper.GameMasterTagMapper;
import com.recommend.common.entity.GameMasterTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
public class GameMasterTagServiceImpl implements GameMasterTagService {
    
    @Autowired
    private GameMasterTagMapper gameMasterTagMapper;
    
    @Override
    public List<GameMasterTag> getGameMasterTagsByMasterId(Long masterId) {
        return gameMasterTagMapper.selectByMasterId(masterId);
    }
    
    @Override
    public List<GameMasterTag> getGameMasterTagsByTagId(Long tagId) {
        return gameMasterTagMapper.selectByTagId(tagId);
    }
    
    @Override
    public List<GameMasterTag> getGameMasterTagsByWeightRange(Double minWeight, Double maxWeight) {
        return gameMasterTagMapper.selectByWeightRange(minWeight, maxWeight);
    }
    
    @Override
    @Transactional
    public void addGameMasterTag(GameMasterTag gameMasterTag) {
        gameMasterTagMapper.insert(gameMasterTag);
    }
    
    @Override
    @Transactional
    public void updateGameMasterTagWeight(Long masterId, Long tagId, Double weight) {
        gameMasterTagMapper.updateWeight(masterId, tagId, weight);
    }
    
    @Override
    @Transactional
    public void deleteGameMasterTag(Long masterId, Long tagId) {
        gameMasterTagMapper.deleteByMasterIdAndTagId(masterId, tagId);
    }
    
    @Override
    @Transactional
    public void batchAddGameMasterTags(List<GameMasterTag> gameMasterTags) {
        for (GameMasterTag gameMasterTag : gameMasterTags) {
            gameMasterTagMapper.insert(gameMasterTag);
        }
    }
    
    @Override
    @Transactional
    public void batchDeleteGameMasterTags(Long masterId, List<Long> tagIds) {
        for (Long tagId : tagIds) {
            gameMasterTagMapper.deleteByMasterIdAndTagId(masterId, tagId);
        }
    }
} 