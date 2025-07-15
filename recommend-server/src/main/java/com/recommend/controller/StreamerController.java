package com.recommend.controller;

import com.recommend.common.entity.Streamer;
import com.recommend.service.StreamerService;
import com.recommend.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/streamers")
@Slf4j
public class StreamerController {
    
    @Autowired
    private StreamerService streamerService;
    
    @GetMapping("/{id}")
    public Result<Streamer> getStreamerById(@PathVariable Long id) {
        Streamer streamer = streamerService.getStreamerById(id);
        return Result.success(streamer);
    }
    
    @GetMapping("/list")
    public Result<List<Streamer>> getStreamerList() {
        List<Streamer> streamers = streamerService.getStreamerList();
        return Result.success(streamers);
    }
    
    @GetMapping("/search")
    public Result<List<Streamer>> searchStreamers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        List<Streamer> streamers = streamerService.searchStreamers(keyword, status);
        return Result.success(streamers);
    }
    
    @GetMapping("/category/{categoryId}")
    public Result<List<Streamer>> getStreamersByCategory(@PathVariable Long categoryId) {
        List<Streamer> streamers = streamerService.getStreamersByCategory(categoryId);
        return Result.success(streamers);
    }
    
    @GetMapping("/region/{region}")
    public Result<List<Streamer>> getStreamersByRegion(@PathVariable String region) {
        List<Streamer> streamers = streamerService.getStreamersByRegion(region);
        return Result.success(streamers);
    }
    
    @PostMapping
    public Result<String> createStreamer(@RequestBody Streamer streamer) {
        streamerService.createStreamer(streamer);
        return Result.success("主播创建成功");
    }
    
    @PutMapping("/{id}")
    public Result<String> updateStreamer(@PathVariable Long id, @RequestBody Streamer streamer) {
        streamer.setId(id);
        streamerService.updateStreamer(streamer);
        return Result.success("主播信息更新成功");
    }
    
    @PutMapping("/{id}/status")
    public Result<String> updateStreamerStatus(@PathVariable Long id, @RequestParam Integer status) {
        streamerService.updateStreamerStatus(id, status);
        return Result.success("主播状态更新成功");
    }
    
    @PutMapping("/{id}/score")
    public Result<String> updateStreamerScore(@PathVariable Long id, @RequestParam Double score) {
        streamerService.updateStreamerScore(id, score);
        return Result.success("主播评分更新成功");
    }
    
    @DeleteMapping("/{id}")
    public Result<String> deleteStreamer(@PathVariable Long id) {
        streamerService.deleteStreamer(id);
        return Result.success("主播删除成功");
    }
    
    @GetMapping("/{id}/profile")
    public Result<Streamer> getStreamerProfile(@PathVariable Long id) {
        Streamer streamer = streamerService.getStreamerProfile(id);
        return Result.success(streamer);
    }
    
    @GetMapping("/{id}/tags")
    public Result<List<String>> getStreamerTags(@PathVariable Long id) {
        List<String> tags = streamerService.getStreamerTags(id);
        return Result.success(tags);
    }
    
    @GetMapping("/{id}/categories")
    public Result<List<String>> getStreamerCategories(@PathVariable Long id) {
        List<String> categories = streamerService.getStreamerCategories(id);
        return Result.success(categories);
    }
    
    @GetMapping("/recommend")
    public Result<List<Streamer>> recommendStreamers(
            @RequestParam Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<Streamer> streamers = streamerService.recommendStreamers(userId, categoryId, limit);
        return Result.success(streamers);
    }
} 