package com.recommend.controller;

import com.recommend.common.entity.UserTag;
import com.recommend.service.UserTagService;
import com.recommend.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/user-tags")
@Slf4j
public class UserTagController {
    
    @Autowired
    private UserTagService userTagService;
    
    @GetMapping("/{id}")
    public Result<UserTag> getUserTagById(@PathVariable Long id) {
        UserTag userTag = userTagService.getUserTagById(id);
        return Result.success(userTag);
    }
    
    @GetMapping("/user/{userId}")
    public Result<List<UserTag>> getUserTagsByUserId(@PathVariable Long userId) {
        List<UserTag> userTags = userTagService.getUserTagsByUserId(userId);
        return Result.success(userTags);
    }
    
    @GetMapping("/tag/{tagId}")
    public Result<List<UserTag>> getUserTagsByTagId(@PathVariable Long tagId) {
        List<UserTag> userTags = userTagService.getUserTagsByTagId(tagId);
        return Result.success(userTags);
    }
    
    @GetMapping("/weight")
    public Result<List<UserTag>> getUserTagsByWeightRange(
            @RequestParam Double minWeight,
            @RequestParam Double maxWeight) {
        List<UserTag> userTags = userTagService.getUserTagsByWeightRange(minWeight, maxWeight);
        return Result.success(userTags);
    }
    
    @PostMapping
    public Result<Void> addUserTag(@RequestBody UserTag userTag) {
        userTagService.addUserTag(userTag);
        return Result.success();
    }
    
    @PutMapping("/{id}")
    public Result<Void> updateUserTag(@PathVariable Long id, @RequestBody UserTag userTag) {
        userTag.setId(id);
        userTagService.updateUserTag(userTag);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteUserTag(@PathVariable Long id) {
        userTagService.deleteUserTag(id);
        return Result.success();
    }
    
    @PostMapping("/batch")
    public Result<Void> batchAddUserTags(@RequestBody List<UserTag> userTags) {
        userTagService.batchAddUserTags(userTags);
        return Result.success();
    }
    
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteUserTags(@RequestBody List<Long> ids) {
        userTagService.batchDeleteUserTags(ids);
        return Result.success();
    }
} 