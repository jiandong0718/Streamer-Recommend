package com.recommend.monitor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liujiandong
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendMonitorService {
    
    private final RecommendMetricsCollector metricsCollector;
    private static final String METRICS_DIR = "metrics";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    private RecommendMetricsSnapshot latestSnapshot;
    
    /**
     * 获取最新的指标快照
     */
    public RecommendMetricsSnapshot getLatestMetricsSnapshot() {
        if (latestSnapshot == null) {
            latestSnapshot = metricsCollector.getMetricsSnapshot();
        }
        return latestSnapshot;
    }
    
    /**
     * 每小时收集一次指标
     */
    @Scheduled(cron = "0 0 * * * *")
    public void collectHourlyMetrics() {
        try {
            RecommendMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
            saveMetricsToFile(snapshot, "hourly");
            log.info("Hourly metrics collected and saved");
        } catch (Exception e) {
            log.error("Error collecting hourly metrics", e);
        }
    }
    
    /**
     * 每天收集一次指标
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void collectDailyMetrics() {
        try {
            RecommendMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
            saveMetricsToFile(snapshot, "daily");
            log.info("Daily metrics collected and saved");
            
            // 重置指标收集器
            metricsCollector.resetMetrics();
        } catch (Exception e) {
            log.error("Error collecting daily metrics", e);
        }
    }
    
    /**
     * 将指标保存到文件
     */
    private void saveMetricsToFile(RecommendMetricsSnapshot snapshot, String type) throws IOException {
        // 创建指标目录
        Path metricsPath = Paths.get(METRICS_DIR, type);
        Files.createDirectories(metricsPath);
        
        // 生成文件名
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String fileName = String.format("%s_metrics.txt", timestamp);
        Path filePath = metricsPath.resolve(fileName);
        
        // 写入指标报告
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            writer.write(snapshot.generateReport());
        }
        
        // 保留最近30天的文件
        cleanupOldFiles(metricsPath);
    }
    
    /**
     * 清理旧文件
     */
    private void cleanupOldFiles(Path directory) throws IOException {
        Files.list(directory)
            .filter(path -> {
                try {
                    return Files.getLastModifiedTime(path).toMillis() < 
                           System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
                } catch (IOException e) {
                    return false;
                }
            })
            .forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    log.error("Error deleting old metrics file: " + path, e);
                }
            });
    }
    
    /**
     * 获取最新的指标报告
     */
    public String getLatestMetricsReport(String type) {
        try {
            Path metricsPath = Paths.get(METRICS_DIR, type);
            if (!Files.exists(metricsPath)) {
                return "No metrics available";
            }
            
            return Files.list(metricsPath)
                .max((p1, p2) -> {
                    try {
                        return Files.getLastModifiedTime(p1).compareTo(Files.getLastModifiedTime(p2));
                    } catch (IOException e) {
                        return 0;
                    }
                })
                .map(path -> {
                    try {
                        return new String(Files.readAllBytes(path));
                    } catch (IOException e) {
                        return "Error reading metrics file";
                    }
                })
                .orElse("No metrics available");
        } catch (IOException e) {
            log.error("Error getting latest metrics report", e);
            return "Error retrieving metrics";
        }
    }

    public Map<String, Object> getRecentAlerts(int count) {
        return new HashMap<>();
    }

    public Map<String, Object> getSystemResources() {
        return new HashMap<>();
    }

    public Map<String, Object> getMetrics(String type) {
        return null;
    }

    public void collectMetrics() {

    }

    public void cleanupData() {

    }
}