package com.recommend.controller;

import com.recommend.common.entity.GameMaster;
import com.recommend.service.GameMasterService;
import com.recommend.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/game-masters")
@Slf4j
public class GameMasterController {
    
    @Autowired
    private GameMasterService gameMasterService;
    
    @GetMapping("/{id}")
    public Result<GameMaster> getGameMasterById(@PathVariable Long id) {
        GameMaster gameMaster = gameMasterService.getGameMasterById(id);
        return Result.success(gameMaster);
    }
    
    @GetMapping("/list")
    public Result<List<GameMaster>> getGameMasterList() {
        List<GameMaster> gameMasters = gameMasterService.getGameMasterList();
        return Result.success(gameMasters);
    }
    
    @GetMapping("/search")
    public Result<List<GameMaster>> searchGameMasters(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        List<GameMaster> gameMasters = gameMasterService.searchGameMasters(keyword, status);
        return Result.success(gameMasters);
    }
    
    @PostMapping
    public Result<Void> createGameMaster(@RequestBody GameMaster gameMaster) {
        gameMasterService.createGameMaster(gameMaster);
        return Result.success();
    }
    
    @PutMapping("/{id}")
    public Result<Void> updateGameMaster(@PathVariable Long id, @RequestBody GameMaster gameMaster) {
        gameMaster.setId(id);
        gameMasterService.updateGameMaster(gameMaster);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteGameMaster(@PathVariable Long id) {
        gameMasterService.deleteGameMaster(id);
        return Result.success();
    }
    
    @GetMapping("/{id}/profile")
    public Result<GameMaster> getGameMasterProfile(@PathVariable Long id) {
        GameMaster gameMaster = gameMasterService.getGameMasterProfile(id);
        return Result.success(gameMaster);
    }
    
    @GetMapping("/{id}/tags")
    public Result<List<String>> getGameMasterTags(@PathVariable Long id) {
        List<String> tags = gameMasterService.getGameMasterTags(id);
        return Result.success(tags);
    }
    
    @GetMapping("/{id}/games")
    public Result<List<String>> getGameMasterGames(@PathVariable Long id) {
        List<String> games = gameMasterService.getGameMasterGames(id);
        return Result.success(games);
    }
    
    @GetMapping("/{id}/orders")
    public Result<List<String>> getGameMasterOrders(@PathVariable Long id) {
        List<String> orders = gameMasterService.getGameMasterOrders(id);
        return Result.success(orders);
    }
    
    @GetMapping("/recommend")
    public Result<List<GameMaster>> recommendGameMasters(
            @RequestParam Long userId,
            @RequestParam(required = false) Long gameId,
            @RequestParam(required = false) Integer limit) {
        List<GameMaster> gameMasters = gameMasterService.recommendGameMasters(userId, gameId, limit);
        return Result.success(gameMasters);
    }
} 