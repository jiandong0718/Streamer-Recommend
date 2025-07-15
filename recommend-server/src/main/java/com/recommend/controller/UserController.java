package com.recommend.controller;

import com.recommend.common.entity.User;
import com.recommend.service.UserService;
import com.recommend.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return Result.success(user);
    }
    
    @GetMapping("/list")
    public Result<List<User>> getUserList() {
        List<User> users = userService.getUserList();
        return Result.success(users);
    }
    
    @GetMapping("/search")
    public Result<List<User>> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        List<User> users = userService.searchUsers(keyword, status);
        return Result.success(users);
    }
    
    @PostMapping
    public Result<Void> createUser(@RequestBody User user) {
        userService.createUser(user);
        return Result.<Void>success();
    }
    
    @PutMapping("/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return Result.<Void>success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.<Void>success();
    }
    
    @GetMapping("/{id}/profile")
    public Result<User> getUserProfile(@PathVariable Long id) {
        User user = userService.getUserProfile(id);
        return Result.success(user);
    }
    
    @GetMapping("/{id}/tags")
    public Result<List<String>> getUserTags(@PathVariable Long id) {
        List<String> tags = userService.getUserTags(id);
        return Result.success(tags);
    }
    
    @GetMapping("/{id}/behaviors")
    public Result<List<String>> getUserBehaviors(@PathVariable Long id) {
        List<String> behaviors = userService.getUserBehaviors(id);
        return Result.success(behaviors);
    }
    
    @GetMapping("/{id}/orders")
    public Result<List<String>> getUserOrders(@PathVariable Long id) {
        List<String> orders = userService.getUserOrders(id);
        return Result.success(orders);
    }
} 