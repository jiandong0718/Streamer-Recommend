package com.recommend.monitor;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendAlertService {

    private final RecommendMonitorService monitorService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 告警阈值配置
    private static final double ERROR_RATE_THRESHOLD = 0.1; // 10%错误率
    private static final double RESPONSE_TIME_THRESHOLD = 1000.0; // 1秒响应时间
    private static final double CACHE_HIT_RATE_THRESHOLD = 0.8; // 80%缓存命中率
    private static final double MEMORY_USAGE_THRESHOLD = 0.85; // 85%内存使用率
    
    // 告警历史记录
    private final Map<String, Alert> alertHistory = new ConcurrentHashMap<>();
    
    /**
     * 检查系统健康状态并触发告警
     */
    public void checkSystemHealth() {
        try {
            RecommendMetricsSnapshot snapshot = monitorService.getLatestMetricsSnapshot();
            
            // 检查错误率
            checkErrorRate(snapshot);
            
            // 检查响应时间
            checkResponseTime(snapshot);
            
            // 检查缓存命中率
            checkCacheHitRate(snapshot);
            
            // 检查内存使用率
            checkMemoryUsage(snapshot);
            
        } catch (Exception e) {
            log.error("检查系统健康状态时发生错误", e);
            triggerAlert("SYSTEM_CHECK_ERROR", "HIGH", "系统健康检查失败: " + e.getMessage());
        }
    }
    
    private void checkErrorRate(RecommendMetricsSnapshot snapshot) {
        double errorRate = snapshot.getErrorRate();
        if (errorRate > ERROR_RATE_THRESHOLD) {
            triggerAlert("HIGH_ERROR_RATE", "HIGH", 
                String.format("错误率过高: %.2f%% (阈值: %.2f%%)", 
                    errorRate * 100, ERROR_RATE_THRESHOLD * 100));
        }
    }
    
    private void checkResponseTime(RecommendMetricsSnapshot snapshot) {
        double avgResponseTime = snapshot.getAverageResponseTime();
        if (avgResponseTime > RESPONSE_TIME_THRESHOLD) {
            triggerAlert("HIGH_RESPONSE_TIME", "MEDIUM", 
                String.format("响应时间过长: %.2fms (阈值: %.2fms)", 
                    avgResponseTime, RESPONSE_TIME_THRESHOLD));
        }
    }
    
    private void checkCacheHitRate(RecommendMetricsSnapshot snapshot) {
        double cacheHitRate = snapshot.getCacheHitRate();
        if (cacheHitRate < CACHE_HIT_RATE_THRESHOLD) {
            triggerAlert("LOW_CACHE_HIT_RATE", "MEDIUM", 
                String.format("缓存命中率过低: %.2f%% (阈值: %.2f%%)", 
                    cacheHitRate * 100, CACHE_HIT_RATE_THRESHOLD * 100));
        }
    }
    
    private void checkMemoryUsage(RecommendMetricsSnapshot snapshot) {
        double memoryUsage = snapshot.getMemoryUsage();
        if (memoryUsage > MEMORY_USAGE_THRESHOLD) {
            triggerAlert("HIGH_MEMORY_USAGE", "HIGH", 
                String.format("内存使用率过高: %.2f%% (阈值: %.2f%%)", 
                    memoryUsage * 100, MEMORY_USAGE_THRESHOLD * 100));
        }
    }
    
    /**
     * 触发告警
     */
    private void triggerAlert(String alertId, String level, String message) {
        Alert alert = Alert.builder()
            .id(alertId)
            .level(level)
            .message(message)
            .timestamp(LocalDateTime.now())
            .source("RecommendSystem")
            .resolved(false)
            .build();
        
        alertHistory.put(alertId, alert);
        
        // 发送告警通知
        sendAlertNotification(alert);
        
        // 记录告警日志
        log.warn("告警触发: [{}] {} - {}", level, alertId, message);
    }
    
    /**
     * 发送告警通知
     */
    void sendAlertNotification(Alert alert) {
        // 这里可以集成邮件、短信、钉钉等通知方式
        String notificationMessage = String.format(
            "告警通知\n" +
            "级别: %s\n" +
            "时间: %s\n" +
            "消息: %s\n" +
            "来源: %s",
            alert.getLevel(),
            alert.getTimestamp().format(DATE_FORMATTER),
            alert.getMessage(),
            alert.getSource()
        );
        
        // 根据告警级别选择通知方式
        if ("HIGH".equals(alert.getLevel())) {
            // 高级别告警立即通知
            log.error("高级别告警: {}", notificationMessage);
            // sendSmsNotification(notificationMessage);
            // sendEmailNotification(notificationMessage);
        } else {
            // 中低级别告警延迟通知
            log.warn("中低级别告警: {}", notificationMessage);
            // scheduleDelayedNotification(notificationMessage);
        }
    }
    
    /**
     * 解决告警
     */
    public void resolveAlert(String alertId) {
        Alert alert = alertHistory.get(alertId);
        if (alert != null) {
            alert.setResolved(true);
            log.info("告警已解决: {}", alertId);
        }
    }
    
    /**
     * 获取未解决的告警
     */
    public List<Alert> getUnresolvedAlerts() {
        return alertHistory.values().stream()
            .filter(alert -> !alert.isResolved())
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * 获取告警历史
     */
    public List<Alert> getAlertHistory() {
        return new ArrayList<>(alertHistory.values());
    }
    
    /**
     * 清理过期告警
     */
    public void cleanupExpiredAlerts() {
        LocalDateTime expireTime = LocalDateTime.now().minusDays(7);
        alertHistory.entrySet().removeIf(entry -> 
            entry.getValue().getTimestamp().isBefore(expireTime));
    }

    public void checkAlerts() {

    }

    public List<Alert> checkSystemStatus() {
        return null;
    }

    @Data
    @Builder
    public static class Alert {
        private String id;
        private String level;
        private String title;
        private String message;
        private LocalDateTime timestamp;
        private String source;
        private boolean resolved;
    }
} 