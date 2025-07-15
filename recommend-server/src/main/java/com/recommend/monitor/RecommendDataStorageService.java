package com.recommend.monitor;

import com.recommend.monitor.RecommendMetricsSnapshot;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendDataStorageService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    // Redis key前缀
    private static final String METRICS_KEY_PREFIX = "recommend:metrics:";
    private static final String SNAPSHOT_KEY_PREFIX = "recommend:snapshot:";
    private static final String ALERT_KEY_PREFIX = "recommend:alert:";
    private static final String RESOURCE_KEY_PREFIX = "recommend:resource:";
    
    // 数据过期时间
    private static final long METRICS_EXPIRATION = 7; // 7天
    private static final long SNAPSHOT_EXPIRATION = 30; // 30天
    private static final long ALERT_EXPIRATION = 90; // 90天
    private static final long RESOURCE_EXPIRATION = 7; // 7天
    
    /**
     * 存储指标数据
     */
    public void storeMetrics(String type, RecommendMetricsSnapshot snapshot) {
        String key = generateMetricsKey(type);
        try {
            redisTemplate.opsForValue().set(key, snapshot, METRICS_EXPIRATION, TimeUnit.DAYS);
            log.info("Stored metrics data for type: {}", type);
        } catch (Exception e) {
            log.error("Error storing metrics data", e);
        }
    }
    
    /**
     * 获取指标数据
     */
    public RecommendMetricsSnapshot getMetrics(String type) {
        String key = generateMetricsKey(type);
        try {
            return (RecommendMetricsSnapshot) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Error retrieving metrics data", e);
            return null;
        }
    }
    
    /**
     * 存储快照数据
     */
    public void storeSnapshot(RecommendMetricsSnapshot snapshot) {
        String key = generateSnapshotKey();
        try {
            redisTemplate.opsForValue().set(key, snapshot, SNAPSHOT_EXPIRATION, TimeUnit.DAYS);
            log.info("Stored metrics snapshot");
        } catch (Exception e) {
            log.error("Error storing metrics snapshot", e);
        }
    }
    
    /**
     * 获取最新的快照数据
     */
    public RecommendMetricsSnapshot getLatestSnapshot() {
        try {
            Set<String> keys = redisTemplate.keys(SNAPSHOT_KEY_PREFIX + "*");
            if (keys == null || keys.isEmpty()) {
                return null;
            }
            
            String latestKey = keys.stream()
                .max(Comparator.naturalOrder())
                .orElse(null);
                
            return latestKey != null ? 
                (RecommendMetricsSnapshot) redisTemplate.opsForValue().get(latestKey) : null;
        } catch (Exception e) {
            log.error("Error retrieving latest snapshot", e);
            return null;
        }
    }
    
    /**
     * 存储告警数据
     */
    public void storeAlert(RecommendAlertService.Alert alert) {
        String key = generateAlertKey(alert);
        try {
            redisTemplate.opsForValue().set(key, alert, ALERT_EXPIRATION, TimeUnit.DAYS);
            log.info("Stored alert: {}", alert);
        } catch (Exception e) {
            log.error("Error storing alert", e);
        }
    }
    
    /**
     * 获取最近的告警数据
     */
    public List<RecommendAlertService.Alert> getRecentAlerts(int count) {
        try {
            Set<String> keys = redisTemplate.keys(ALERT_KEY_PREFIX + "*");
            if (keys == null || keys.isEmpty()) {
                return Collections.emptyList();
            }
            
            return keys.stream()
                .sorted(Comparator.reverseOrder())
                .limit(count)
                .map(key -> (RecommendAlertService.Alert) redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .toList();
        } catch (Exception e) {
            log.error("Error retrieving recent alerts", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * 存储系统资源使用情况
     */
    public void storeSystemResourceUsage(SystemResourceMonitor.SystemResourceUsage usage) {
        String key = generateResourceKey();
        try {
            redisTemplate.opsForValue().set(key, usage, RESOURCE_EXPIRATION, TimeUnit.DAYS);
            log.info("Stored system resource usage: {}", usage);
        } catch (Exception e) {
            log.error("Error storing system resource usage", e);
        }
    }
    
    /**
     * 获取系统资源使用历史
     */
    public List<SystemResourceMonitor.SystemResourceUsage> getSystemResourceHistory(int hours) {
        try {
            Set<String> keys = redisTemplate.keys(RESOURCE_KEY_PREFIX + "*");
            if (keys == null || keys.isEmpty()) {
                return Collections.emptyList();
            }
            
            LocalDateTime cutoff = LocalDateTime.now().minusHours(hours);
            
            return keys.stream()
                .filter(key -> {
                    String timestamp = key.substring(key.lastIndexOf(":") + 1);
                    LocalDateTime keyTime = LocalDateTime.parse(timestamp, DATE_FORMATTER);
                    return !keyTime.isBefore(cutoff);
                })
                .sorted()
                .map(key -> (SystemResourceMonitor.SystemResourceUsage) redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .toList();
        } catch (Exception e) {
            log.error("Error retrieving system resource history", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * 清理过期数据
     */
    public void cleanupExpiredData() {
        try {
            // Redis会自动处理过期数据
            log.info("Cleanup completed");
        } catch (Exception e) {
            log.error("Error during cleanup", e);
        }
    }
    
    /**
     * 生成指标数据的key
     */
    private String generateMetricsKey(String type) {
        return METRICS_KEY_PREFIX + type + ":" + LocalDateTime.now().format(DATE_FORMATTER);
    }
    
    /**
     * 生成快照数据的key
     */
    private String generateSnapshotKey() {
        return SNAPSHOT_KEY_PREFIX + LocalDateTime.now().format(DATE_FORMATTER);
    }
    
    /**
     * 生成告警数据的key
     */
    private String generateAlertKey(RecommendAlertService.Alert alert) {
        return ALERT_KEY_PREFIX + alert.getLevel() + ":" + 
               alert.getTimestamp().format(DATE_FORMATTER);
    }
    
    /**
     * 生成资源使用数据的key
     */
    private String generateResourceKey() {
        return RESOURCE_KEY_PREFIX + LocalDateTime.now().format(DATE_FORMATTER);
    }
    
    /**
     * 获取时间序列数据
     */
    public List<RecommendMetricsSnapshot> getTimeSeriesData(String type, int hours) {
        try {
            String pattern = METRICS_KEY_PREFIX + type + ":*";
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys == null || keys.isEmpty()) {
                return Collections.emptyList();
            }
            
            LocalDateTime cutoff = LocalDateTime.now().minusHours(hours);
            
            return keys.stream()
                .filter(key -> {
                    String timestamp = key.substring(key.lastIndexOf(":") + 1);
                    LocalDateTime keyTime = LocalDateTime.parse(timestamp, DATE_FORMATTER);
                    return !keyTime.isBefore(cutoff);
                })
                .sorted()
                .map(key -> (RecommendMetricsSnapshot) redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .toList();
        } catch (Exception e) {
            log.error("Error retrieving time series data", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * 获取聚合数据
     */
    public Map<String, Double> getAggregatedMetrics(String type, int hours) {
        List<RecommendMetricsSnapshot> snapshots = getTimeSeriesData(type, hours);
        if (snapshots.isEmpty()) {
            return Collections.emptyMap();
        }
        
        Map<String, Double> aggregated = new HashMap<>();
        
        // 计算平均值
        aggregated.put("avgPrecision", snapshots.stream()
            .mapToDouble(RecommendMetricsSnapshot::getAveragePrecision)
            .average()
            .orElse(0.0));
            
        aggregated.put("avgRecall", snapshots.stream()
            .mapToDouble(RecommendMetricsSnapshot::getAverageRecall)
            .average()
            .orElse(0.0));
            
        aggregated.put("avgResponseTime", snapshots.stream()
            .mapToDouble(RecommendMetricsSnapshot::getAverageResponseTime)
            .average()
            .orElse(0.0));
            
        // 计算最大值
        aggregated.put("maxPrecision", snapshots.stream()
            .mapToDouble(RecommendMetricsSnapshot::getAveragePrecision)
            .max()
            .orElse(0.0));
            
        aggregated.put("maxRecall", snapshots.stream()
            .mapToDouble(RecommendMetricsSnapshot::getAverageRecall)
            .max()
            .orElse(0.0));
            
        // 计算最小值
        aggregated.put("minPrecision", snapshots.stream()
            .mapToDouble(RecommendMetricsSnapshot::getAveragePrecision)
            .min()
            .orElse(0.0));
            
        aggregated.put("minRecall", snapshots.stream()
            .mapToDouble(RecommendMetricsSnapshot::getAverageRecall)
            .min()
            .orElse(0.0));
            
        return aggregated;
    }
} 