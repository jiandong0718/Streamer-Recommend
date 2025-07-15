package com.recommend.controller;

import com.recommend.common.entity.Tag;
import com.recommend.service.TagService;
import com.recommend.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
@Slf4j
public class TagController {
    
    @Autowired
    private TagService tagService;
    
    @GetMapping("/{id}")
    public Result<Tag> getTagById(@PathVariable Long id) {
        Tag tag = tagService.getTagById(id);
        return Result.success(tag);
    }
    
    @GetMapping("/list")
    public Result<List<Tag>> getTagList() {
        List<Tag> tags = tagService.getTagList();
        return Result.success(tags);
    }
    
    @GetMapping("/search")
    public Result<List<Tag>> searchTags(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String category) {
        List<Tag> tags = tagService.searchTags(keyword, type, category);
        return Result.success(tags);
    }
    
    @PostMapping
    public Result<Void> createTag(@RequestBody Tag tag) {
        tagService.createTag(tag);
        return Result.success();
    }
    
    @PutMapping("/{id}")
    public Result<Void> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        tag.setId(id);
        tagService.updateTag(tag);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success();
    }
    
    @GetMapping("/{id}/users")
    public Result<List<String>> getTagUsers(@PathVariable Long id) {
        List<String> users = tagService.getTagUsers(id);
        return Result.success(users);
    }
    
    @GetMapping("/{id}/masters")
    public Result<List<String>> getTagMasters(@PathVariable Long id) {
        List<String> masters = tagService.getTagMasters(id);
        return Result.success(masters);
    }
    
    @GetMapping("/{id}/weight")
    public Result<Double> getTagWeight(@PathVariable Long id) {
        Double weight = tagService.getTagWeight(id);
        return Result.success(weight);
    }
    
    @GetMapping("/{id}/similar")
    public Result<List<Tag>> getSimilarTags(
            @PathVariable Long id,
            @RequestParam(required = false) Integer limit) {
        List<Tag> tags = tagService.getSimilarTags(id, limit);
        return Result.success(tags);
    }
} 