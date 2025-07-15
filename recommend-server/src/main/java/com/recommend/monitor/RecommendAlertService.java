package com.recommend.monitor;

import com.recommend.monitor.RecommendMetricsSnapshot;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendAlertService {
    
    private final RecommendMonitorService monitorService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 告警阈值配置
    private static final double ERROR_RATE_THRESHOLD = 0.1; // 10%错误率
    private static final double RESPONSE_TIME_THRESHOLD = 1000.0; // 1000ms
    private static final double CACHE_HIT_RATE_THRESHOLD = 0.5; // 50%缓存命中率
    private static final double QUALITY_METRIC_THRESHOLD = 0.6; // 60%质量指标
    
    // 告警级别
    public enum AlertLevel {
        INFO, WARNING, ERROR, CRITICAL
    }
    
    /**
     * 检查系统状态并生成告警
     */
    public List<Alert> checkSystemStatus() {
        List<Alert> alerts = new ArrayList<>();
        RecommendMetricsSnapshot snapshot = monitorService.getLatestMetricsSnapshot();
        
        // 检查错误率
        if (snapshot.getFailureRate() > ERROR_RATE_THRESHOLD) {
            alerts.add(createAlert(
                "High Error Rate",
                String.format("Error rate is %.2f%%, exceeding threshold of %.2f%%",
                    snapshot.getFailureRate() * 100,
                    ERROR_RATE_THRESHOLD * 100),
                AlertLevel.ERROR
            ));
        }
        
        // 检查响应时间
        if (snapshot.getAverageResponseTime() > RESPONSE_TIME_THRESHOLD) {
            alerts.add(createAlert(
                "High Response Time",
                String.format("Average response time is %.2fms, exceeding threshold of %.2fms",
                    snapshot.getAverageResponseTime(),
                    RESPONSE_TIME_THRESHOLD),
                AlertLevel.WARNING
            ));
        }
        
        // 检查缓存命中率
        if (snapshot.getCacheHitRate() < CACHE_HIT_RATE_THRESHOLD) {
            alerts.add(createAlert(
                "Low Cache Hit Rate",
                String.format("Cache hit rate is %.2f%%, below threshold of %.2f%%",
                    snapshot.getCacheHitRate() * 100,
                    CACHE_HIT_RATE_THRESHOLD * 100),
                AlertLevel.WARNING
            ));
        }
        
        // 检查推荐质量指标
        checkQualityMetrics(snapshot, alerts);
        
        return alerts;
    }
    
    /**
     * 检查推荐质量指标
     */
    private void checkQualityMetrics(RecommendMetricsSnapshot snapshot, List<Alert> alerts) {
        // 检查准确率
        if (snapshot.getAveragePrecision() < QUALITY_METRIC_THRESHOLD) {
            alerts.add(createAlert(
                "Low Precision",
                String.format("Average precision is %.2f, below threshold of %.2f",
                    snapshot.getAveragePrecision(),
                    QUALITY_METRIC_THRESHOLD),
                AlertLevel.WARNING
            ));
        }
        
        // 检查召回率
        if (snapshot.getAverageRecall() < QUALITY_METRIC_THRESHOLD) {
            alerts.add(createAlert(
                "Low Recall",
                String.format("Average recall is %.2f, below threshold of %.2f",
                    snapshot.getAverageRecall(),
                    QUALITY_METRIC_THRESHOLD),
                AlertLevel.WARNING
            ));
        }
        
        // 检查多样性
        if (snapshot.getDiversity() < QUALITY_METRIC_THRESHOLD) {
            alerts.add(createAlert(
                "Low Diversity",
                String.format("Diversity score is %.2f, below threshold of %.2f",
                    snapshot.getDiversity(),
                    QUALITY_METRIC_THRESHOLD),
                AlertLevel.INFO
            ));
        }
    }
    
    /**
     * 创建告警对象
     */
    private Alert createAlert(String title, String message, AlertLevel level) {
        return Alert.builder()
            .title(title)
            .message(message)
            .level(level)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    /**
     * 告警对象
     */
    @Data
    @Builder
    public static class Alert {
        private String title;
        private String message;
        private AlertLevel level;
        private LocalDateTime timestamp;
        
        @Override
        public String toString() {
            return String.format("[%s] %s - %s (%s)",
                level,
                title,
                message,
                timestamp.format(DATE_FORMATTER)
            );
        }
    }
    
    /**
     * 发送告警通知
     */
    public void sendAlertNotification(Alert alert) {
        // 根据告警级别选择通知方式
        switch (alert.getLevel()) {
            case CRITICAL:
                // 发送短信和邮件
                sendSMS(alert);
                sendEmail(alert);
                break;
            case ERROR:
                // 发送邮件
                sendEmail(alert);
                break;
            case WARNING:
                // 记录到日志
                log.warn(alert.toString());
                break;
            case INFO:
                // 记录到日志
                log.info(alert.toString());
                break;
        }
    }
    
    /**
     * 发送短信通知
     */
    private void sendSMS(Alert alert) {
        // TODO: 实现短信发送逻辑
        log.info("Sending SMS alert: {}", alert);
    }
    
    /**
     * 发送邮件通知
     */
    private void sendEmail(Alert alert) {
        // TODO: 实现邮件发送逻辑
        log.info("Sending email alert: {}", alert);
    }
} 