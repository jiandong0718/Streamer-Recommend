package com.recommend.controller;

import com.recommend.common.entity.Game;
import com.recommend.service.GameService;
import com.recommend.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/games")
@Slf4j
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @GetMapping("/{id}")
    public Result<Game> getGameById(@PathVariable Long id) {
        Game game = gameService.getGameById(id);
        return Result.success(game);
    }
    
    @GetMapping("/list")
    public Result<List<Game>> getGameList() {
        List<Game> games = gameService.getGameList();
        return Result.success(games);
    }
    
    @GetMapping("/search")
    public Result<List<Game>> searchGames(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        List<Game> games = gameService.searchGames(keyword, status);
        return Result.success(games);
    }
    
    @PostMapping
    public Result<Void> createGame(@RequestBody Game game) {
        gameService.createGame(game);
        return Result.success();
    }
    
    @PutMapping("/{id}")
    public Result<Void> updateGame(@PathVariable Long id, @RequestBody Game game) {
        game.setId(id);
        gameService.updateGame(game);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return Result.success();
    }
    
    @GetMapping("/{id}/masters")
    public Result<List<String>> getGameMasters(@PathVariable Long id) {
        List<String> masters = gameService.getGameMasters(id);
        return Result.success(masters);
    }
    
    @GetMapping("/{id}/orders")
    public Result<List<String>> getGameOrders(@PathVariable Long id) {
        List<String> orders = gameService.getGameOrders(id);
        return Result.success(orders);
    }
    
    @GetMapping("/recommend")
    public Result<List<Game>> recommendGames(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer limit) {
        List<Game> games = gameService.recommendGames(userId, limit);
        return Result.success(games);
    }
} 