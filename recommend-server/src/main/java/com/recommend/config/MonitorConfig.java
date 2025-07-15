package com.recommend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class MonitorConfig implements SchedulingConfigurer {
    
    private static final int POOL_SIZE = 5;
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }
    
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(POOL_SIZE);
    }
    
    // 监控任务配置
    public static class MonitorTaskConfig {
        // 指标收集间隔（毫秒）
        public static final long METRICS_COLLECTION_INTERVAL = 3600000; // 1小时
        
        // 告警检查间隔（毫秒）
        public static final long ALERT_CHECK_INTERVAL = 300000; // 5分钟
        
        // 数据清理间隔（毫秒）
        public static final long CLEANUP_INTERVAL = 86400000; // 24小时
        
        // 快照保存间隔（毫秒）
        public static final long SNAPSHOT_INTERVAL = 3600000; // 1小时
    }
    
    // 告警阈值配置
    public static class AlertThresholdConfig {
        // 错误率阈值
        public static final double ERROR_RATE_THRESHOLD = 0.1; // 10%
        
        // 响应时间阈值（毫秒）
        public static final double RESPONSE_TIME_THRESHOLD = 1000.0;
        
        // 缓存命中率阈值
        public static final double CACHE_HIT_RATE_THRESHOLD = 0.5; // 50%
        
        // 推荐质量指标阈值
        public static final double QUALITY_METRIC_THRESHOLD = 0.6; // 60%
        
        // 系统负载阈值
        public static final double SYSTEM_LOAD_THRESHOLD = 0.8; // 80%
        
        // 内存使用率阈值
        public static final double MEMORY_USAGE_THRESHOLD = 0.9; // 90%
    }
    
    // 数据存储配置
    public static class StorageConfig {
        // 数据过期时间（天）
        public static final long METRICS_EXPIRATION = 7;
        public static final long SNAPSHOT_EXPIRATION = 30;
        public static final long ALERT_EXPIRATION = 90;
        
        // 数据保留策略
        public static final int MAX_METRICS_RECORDS = 1000;
        public static final int MAX_SNAPSHOT_RECORDS = 100;
        public static final int MAX_ALERT_RECORDS = 500;
        
        // 数据压缩阈值
        public static final int COMPRESSION_THRESHOLD = 1000;
    }
    
    // 可视化配置
    public static class VisualizationConfig {
        // 图表配置
        public static final int CHART_WIDTH = 800;
        public static final int CHART_HEIGHT = 400;
        
        // 时间范围选项（小时）
        public static final int[] TIME_RANGES = {1, 6, 12, 24, 48, 72};
        
        // 数据点限制
        public static final int MAX_DATA_POINTS = 100;
        
        // 刷新间隔（秒）
        public static final int REFRESH_INTERVAL = 60;
    }
    
    // 通知配置
    public static class NotificationConfig {
        // 邮件配置
        public static final String EMAIL_SENDER = "monitor@example.com";
        public static final String[] EMAIL_RECIPIENTS = {
            "admin@example.com",
            "ops@example.com"
        };
        
        // 短信配置
        public static final String SMS_PROVIDER = "default";
        public static final String[] SMS_RECIPIENTS = {
            "+1234567890",
            "+0987654321"
        };
        
        // 通知重试配置
        public static final int MAX_RETRY_ATTEMPTS = 3;
        public static final long RETRY_DELAY = 300000; // 5分钟
    }
} 