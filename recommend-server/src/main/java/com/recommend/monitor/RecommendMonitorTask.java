package com.recommend.monitor;

import com.recommend.config.MonitorConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendMonitorTask {
    
    private final RecommendMetricsCollector metricsCollector;
    private final RecommendMonitorService monitorService;
    private final RecommendAlertService alertService;
    private final RecommendDataStorageService storageService;
    private final SystemResourceMonitor systemResourceMonitor;
    
    /**
     * 定期收集指标
     */
    @Scheduled(fixedRate = MonitorConfig.MonitorTaskConfig.METRICS_COLLECTION_INTERVAL)
    public void collectMetrics() {
        try {
            log.info("Starting metrics collection");
            
            // 收集指标
            RecommendMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
            
            // 保存快照
            storageService.storeSnapshot(snapshot);
            
            // 更新监控服务
            monitorService.collectHourlyMetrics();
            
            log.info("Metrics collection completed");
        } catch (Exception e) {
            log.error("Error during metrics collection", e);
        }
    }
    
    /**
     * 定期检查告警
     */
    @Scheduled(fixedRate = MonitorConfig.MonitorTaskConfig.ALERT_CHECK_INTERVAL)
    public void checkAlerts() {
        try {
            log.info("Starting alert check");
            
            // 检查系统状态
            List<RecommendAlertService.Alert> alerts = alertService.checkSystemStatus();
            
            // 处理告警
            for (RecommendAlertService.Alert alert : alerts) {
                // 存储告警
                storageService.storeAlert(alert);
                
                // 发送通知
                alertService.sendAlertNotification(alert);
            }
            
            log.info("Alert check completed, found {} alerts", alerts.size());
        } catch (Exception e) {
            log.error("Error during alert check", e);
        }
    }
    
    /**
     * 定期清理数据
     */
    @Scheduled(fixedRate = MonitorConfig.MonitorTaskConfig.CLEANUP_INTERVAL)
    public void cleanupData() {
        try {
            log.info("Starting data cleanup");
            
            // 清理过期数据
            storageService.cleanupExpiredData();
            
            log.info("Data cleanup completed");
        } catch (Exception e) {
            log.error("Error during data cleanup", e);
        }
    }
    
    /**
     * 定期保存快照
     */
    @Scheduled(fixedRate = MonitorConfig.MonitorTaskConfig.SNAPSHOT_INTERVAL)
    public void saveSnapshot() {
        try {
            log.info("Starting snapshot save");
            
            // 获取当前快照
            RecommendMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
            
            // 保存快照
            storageService.storeSnapshot(snapshot);
            
            log.info("Snapshot save completed");
        } catch (Exception e) {
            log.error("Error during snapshot save", e);
        }
    }
    
    /**
     * 定期检查系统资源
     */
    @Scheduled(fixedRate = 300000) // 每5分钟
    public void checkSystemResources() {
        try {
            log.info("Starting system resource check");
            
            // 获取系统资源使用情况
            SystemResourceMonitor.SystemResourceUsage usage = systemResourceMonitor.getSystemResourceUsage();
            
            // 检查系统负载
            if (usage.getSystemLoad() > MonitorConfig.AlertThresholdConfig.SYSTEM_LOAD_THRESHOLD) {
                alertService.sendAlertNotification(
                    RecommendAlertService.Alert.builder()
                        .title("High System Load")
                        .message(String.format("System load is %.2f, exceeding threshold of %.2f",
                            usage.getSystemLoad(),
                            MonitorConfig.AlertThresholdConfig.SYSTEM_LOAD_THRESHOLD))
                        .level(RecommendAlertService.AlertLevel.WARNING)
                        .build()
                );
            }
            
            // 检查CPU使用率
            if (usage.getCpuUsage() > MonitorConfig.AlertThresholdConfig.SYSTEM_LOAD_THRESHOLD) {
                alertService.sendAlertNotification(
                    RecommendAlertService.Alert.builder()
                        .title("High CPU Usage")
                        .message(String.format("CPU usage is %.2f%%, exceeding threshold of %.2f%%",
                            usage.getCpuUsage() * 100,
                            MonitorConfig.AlertThresholdConfig.SYSTEM_LOAD_THRESHOLD * 100))
                        .level(RecommendAlertService.AlertLevel.WARNING)
                        .build()
                );
            }
            
            // 检查内存使用率
            if (usage.getMemoryUsage() > MonitorConfig.AlertThresholdConfig.MEMORY_USAGE_THRESHOLD) {
                alertService.sendAlertNotification(
                    RecommendAlertService.Alert.builder()
                        .title("High Memory Usage")
                        .message(String.format("Memory usage is %.2f%%, exceeding threshold of %.2f%%",
                            usage.getMemoryUsage() * 100,
                            MonitorConfig.AlertThresholdConfig.MEMORY_USAGE_THRESHOLD * 100))
                        .level(RecommendAlertService.AlertLevel.WARNING)
                        .build()
                );
            }
            
            // 检查死锁线程
            if (usage.getDeadlockedThreadCount() > 0) {
                alertService.sendAlertNotification(
                    RecommendAlertService.Alert.builder()
                        .title("Deadlocked Threads Detected")
                        .message(String.format("Found %d deadlocked threads",
                            usage.getDeadlockedThreadCount()))
                        .level(RecommendAlertService.AlertLevel.ERROR)
                        .build()
                );
            }
            
            // 存储系统资源使用情况
            storageService.storeSystemResourceUsage(usage);
            
            log.info("System resource check completed: {}", usage);
        } catch (Exception e) {
            log.error("Error during system resource check", e);
        }
    }
} 