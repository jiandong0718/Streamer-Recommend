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
    public Result<List<UserTag>> getUserTagsByUserId(@PathVariable Long id) {
        List<UserTag> userTags = userTagService.getUserTagsByUserId(id);
        return Result.success(userTags);
    }

    @GetMapping("/user/{userId}")
    public Result<List<UserTag>> getUserTagsByUserId2(@PathVariable Long userId) {
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

    @PutMapping("/{userId}/{tagId}")
    public Result<Void> updateUserTagWeight(@PathVariable Long userId, @PathVariable Long tagId, @RequestParam Double weight) {
        userTagService.updateUserTagWeight(userId, tagId, weight);
        return Result.success();
    }

    @DeleteMapping("/{userId}/{tagId}")
    public Result<Void> deleteUserTag(@PathVariable Long userId, @PathVariable Long tagId) {
        userTagService.deleteUserTag(userId, tagId);
        return Result.success();
    }

    @PostMapping("/batch")
    public Result<Void> batchAddUserTags(@RequestBody List<UserTag> userTags) {
        userTagService.batchAddUserTags(userTags);
        return Result.success();
    }

    @DeleteMapping("/batch/{userId}")
    public Result<Void> batchDeleteUserTags(@PathVariable Long userId, @RequestBody List<Long> tagIds) {
        userTagService.batchDeleteUserTags(userId, tagIds);
        return Result.success();
    }
} 