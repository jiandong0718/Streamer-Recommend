package com.recommend.controller;

import com.recommend.common.entity.GameMasterGame;
import com.recommend.service.GameMasterGameService;
import com.recommend.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/game-master-games")
@Slf4j
public class GameMasterGameController {
    
    @Autowired
    private GameMasterGameService gameMasterGameService;
    
    @GetMapping("/{id}")
    public Result<GameMasterGame> getGameMasterGameById(@PathVariable Long id) {
        GameMasterGame gameMasterGame = gameMasterGameService.getGameMasterGameById(id);
        return Result.success(gameMasterGame);
    }
    
    @GetMapping("/master/{masterId}")
    public Result<List<GameMasterGame>> getGameMasterGamesByMasterId(@PathVariable Long masterId) {
        List<GameMasterGame> gameMasterGames = gameMasterGameService.getGameMasterGamesByMasterId(masterId);
        return Result.success(gameMasterGames);
    }
    
    @GetMapping("/game/{gameId}")
    public Result<List<GameMasterGame>> getGameMasterGamesByGameId(@PathVariable Long gameId) {
        List<GameMasterGame> gameMasterGames = gameMasterGameService.getGameMasterGamesByGameId(gameId);
        return Result.success(gameMasterGames);
    }
    
    @GetMapping("/score")
    public Result<List<GameMasterGame>> getGameMasterGamesByScoreRange(
            @RequestParam Double minScore,
            @RequestParam Double maxScore) {
        List<GameMasterGame> gameMasterGames = gameMasterGameService.getGameMasterGamesByScoreRange(minScore, maxScore);
        return Result.success(gameMasterGames);
    }
    
    @GetMapping("/order-count")
    public Result<List<GameMasterGame>> getGameMasterGamesByOrderCountRange(
            @RequestParam Integer minOrderCount,
            @RequestParam Integer maxOrderCount) {
        List<GameMasterGame> gameMasterGames = gameMasterGameService.getGameMasterGamesByOrderCountRange(minOrderCount, maxOrderCount);
        return Result.success(gameMasterGames);
    }
    
    @PostMapping
    public Result<Void> addGameMasterGame(@RequestBody GameMasterGame gameMasterGame) {
        gameMasterGameService.addGameMasterGame(gameMasterGame);
        return Result.success();
    }
    
    @PutMapping("/{id}/score")
    public Result<Void> updateGameMasterGameScore(
            @PathVariable Long id,
            @RequestParam Double score) {
        gameMasterGameService.updateGameMasterGameScore(id, score);
        return Result.success();
    }
    
    @PutMapping("/{id}/order-count")
    public Result<Void> updateGameMasterGameOrderCount(
            @PathVariable Long id,
            @RequestParam Integer orderCount) {
        gameMasterGameService.updateGameMasterGameOrderCount(id, orderCount);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteGameMasterGame(@PathVariable Long id) {
        gameMasterGameService.deleteGameMasterGame(id);
        return Result.success();
    }
    
    @PostMapping("/batch")
    public Result<Void> batchAddGameMasterGames(@RequestBody List<GameMasterGame> gameMasterGames) {
        gameMasterGameService.batchAddGameMasterGames(gameMasterGames);
        return Result.success();
    }
    
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteGameMasterGames(@RequestBody List<Long> ids) {
        gameMasterGameService.batchDeleteGameMasterGames(ids);
        return Result.success();
    }
} 