package com.recommend.controller;

import com.recommend.common.entity.GameMasterTag;
import com.recommend.service.GameMasterTagService;
import com.recommend.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/game-master-tags")
@Slf4j
public class GameMasterTagController {
    
    @Autowired
    private GameMasterTagService gameMasterTagService;
    
    @GetMapping("/{id}")
    public Result<GameMasterTag> getGameMasterTagById(@PathVariable Long id) {
        GameMasterTag gameMasterTag = gameMasterTagService.getGameMasterTagById(id);
        return Result.success(gameMasterTag);
    }
    
    @GetMapping("/master/{masterId}")
    public Result<List<GameMasterTag>> getGameMasterTagsByMasterId(@PathVariable Long masterId) {
        List<GameMasterTag> gameMasterTags = gameMasterTagService.getGameMasterTagsByMasterId(masterId);
        return Result.success(gameMasterTags);
    }
    
    @GetMapping("/tag/{tagId}")
    public Result<List<GameMasterTag>> getGameMasterTagsByTagId(@PathVariable Long tagId) {
        List<GameMasterTag> gameMasterTags = gameMasterTagService.getGameMasterTagsByTagId(tagId);
        return Result.success(gameMasterTags);
    }
    
    @GetMapping("/weight")
    public Result<List<GameMasterTag>> getGameMasterTagsByWeightRange(
            @RequestParam Double minWeight,
            @RequestParam Double maxWeight) {
        List<GameMasterTag> gameMasterTags = gameMasterTagService.getGameMasterTagsByWeightRange(minWeight, maxWeight);
        return Result.success(gameMasterTags);
    }
    
    @PostMapping
    public Result<Void> addGameMasterTag(@RequestBody GameMasterTag gameMasterTag) {
        gameMasterTagService.addGameMasterTag(gameMasterTag);
        return Result.success();
    }
    
    @PutMapping("/{id}")
    public Result<Void> updateGameMasterTag(@PathVariable Long id, @RequestBody GameMasterTag gameMasterTag) {
        gameMasterTag.setId(id);
        gameMasterTagService.updateGameMasterTag(gameMasterTag);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteGameMasterTag(@PathVariable Long id) {
        gameMasterTagService.deleteGameMasterTag(id);
        return Result.success();
    }
    
    @PostMapping("/batch")
    public Result<Void> batchAddGameMasterTags(@RequestBody List<GameMasterTag> gameMasterTags) {
        gameMasterTagService.batchAddGameMasterTags(gameMasterTags);
        return Result.success();
    }
    
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteGameMasterTags(@RequestBody List<Long> ids) {
        gameMasterTagService.batchDeleteGameMasterTags(ids);
        return Result.success();
    }
} 