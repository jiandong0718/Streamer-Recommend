package com.recommend.controller;

import com.recommend.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 * 
 * @author liujiandong
 */
@RestController
@RequestMapping("/api/health")
@Slf4j
public class HealthController {

    @Autowired
    private Environment environment;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 基础健康检查
     */
    @GetMapping
    public Result<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        status.put("service", "recommend-server");
        status.put("version", "1.0.0");
        
        return Result.success(status);
    }

    /**
     * 详细健康检查
     */
    @GetMapping("/detailed")
    public Result<Map<String, Object>> detailedHealth() {
        Map<String, Object> health = new HashMap<>();
        
        // 基础信息
        health.put("service", "recommend-server");
        health.put("version", "1.0.0");
        health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        health.put("profiles", environment.getActiveProfiles());
        
        // 检查各个组件状态
        Map<String, Object> components = new HashMap<>();
        
        // 数据库状态
        components.put("database", checkDatabaseHealth());
        
        // Redis状态
        components.put("redis", checkRedisHealth());
        
        // JVM状态
        components.put("jvm", getJvmHealth());
        
        health.put("components", components);
        
        // 整体状态
        boolean allHealthy = components.values().stream()
            .allMatch(component -> {
                if (component instanceof Map) {
                    return "UP".equals(((Map<?, ?>) component).get("status"));
                }
                return false;
            });
        
        health.put("status", allHealthy ? "UP" : "DOWN");
        
        return Result.success(health);
    }

    /**
     * 系统信息
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        
        // 应用信息
        Map<String, Object> app = new HashMap<>();
        app.put("name", "recommend-server");
        app.put("version", "1.0.0");
        app.put("description", "Game Master Recommendation System");
        app.put("encoding", System.getProperty("file.encoding"));
        app.put("java.version", System.getProperty("java.version"));
        app.put("java.vendor", System.getProperty("java.vendor"));
        
        // 系统信息
        Map<String, Object> system = new HashMap<>();
        system.put("os.name", System.getProperty("os.name"));
        system.put("os.version", System.getProperty("os.version"));
        system.put("os.arch", System.getProperty("os.arch"));
        system.put("user.timezone", System.getProperty("user.timezone"));
        
        // Spring Boot信息
        Map<String, Object> spring = new HashMap<>();
        spring.put("profiles.active", environment.getActiveProfiles());
        spring.put("profiles.default", environment.getDefaultProfiles());
        
        info.put("app", app);
        info.put("system", system);
        info.put("spring", spring);
        
        return Result.success(info);
    }

    /**
     * 检查数据库健康状态
     */
    private Map<String, Object> checkDatabaseHealth() {
        Map<String, Object> dbHealth = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                dbHealth.put("status", "UP");
                dbHealth.put("database", connection.getMetaData().getDatabaseProductName());
                dbHealth.put("version", connection.getMetaData().getDatabaseProductVersion());
            } else {
                dbHealth.put("status", "DOWN");
                dbHealth.put("error", "Connection is not valid");
            }
        } catch (Exception e) {
            dbHealth.put("status", "DOWN");
            dbHealth.put("error", e.getMessage());
            log.error("Database health check failed", e);
        }
        
        return dbHealth;
    }

    /**
     * 检查Redis健康状态
     */
    private Map<String, Object> checkRedisHealth() {
        Map<String, Object> redisHealth = new HashMap<>();
        
        try {
            String pong = redisTemplate.getConnectionFactory().getConnection().ping();
            if ("PONG".equals(pong)) {
                redisHealth.put("status", "UP");
            } else {
                redisHealth.put("status", "DOWN");
                redisHealth.put("error", "Ping failed");
            }
        } catch (Exception e) {
            redisHealth.put("status", "DOWN");
            redisHealth.put("error", e.getMessage());
            log.error("Redis health check failed", e);
        }
        
        return redisHealth;
    }

    /**
     * 获取JVM健康状态
     */
    private Map<String, Object> getJvmHealth() {
        Map<String, Object> jvmHealth = new HashMap<>();
        
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        Map<String, Object> memory = new HashMap<>();
        memory.put("max", formatBytes(maxMemory));
        memory.put("total", formatBytes(totalMemory));
        memory.put("free", formatBytes(freeMemory));
        memory.put("used", formatBytes(usedMemory));
        memory.put("usage", String.format("%.2f%%", (double) usedMemory / maxMemory * 100));
        
        jvmHealth.put("status", "UP");
        jvmHealth.put("memory", memory);
        jvmHealth.put("processors", runtime.availableProcessors());
        
        return jvmHealth;
    }

    /**
     * 格式化字节数
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
} 