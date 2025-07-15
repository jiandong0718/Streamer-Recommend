package com.recommend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;

/**
 * 推荐系统主启动类
 *
 * @author liujiandong
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan({"com.recommend.service.mapper", "com.recommend.*.mapper"})
public class RecommendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendApplication.class, args);
    }

    /**
     * 自定义异步任务线程池
     * 重写默认的线程池创建方法
     */
    @Primary
    @Bean(name = "AsyncTaskExecutor", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor asyncTaskExecutor(TaskExecutorBuilder builder) {
        return builder
                .corePoolSize(20)
                .maxPoolSize(50)
                .queueCapacity(100)
                .threadNamePrefix("recommend-async-")
                .keepAlive(Duration.ofMinutes(1))
                .build();
    }

    /**
     * 定时任务线程池
     */
    @Bean(name = "TaskScheduler", destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(8);
        scheduler.setThreadNamePrefix("recommend-task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

    /**
     * 推荐系统配置
     */
    @Bean
    @ConfigurationProperties(prefix = "recommend.system")
    public RecommendSystemConfig recommendSystemConfig() {
        return new RecommendSystemConfig();
    }

    /**
     * 推荐系统配置类
     */
    public static class RecommendSystemConfig {
        private String version = "1.0.0";
        private boolean enableCache = true;
        private boolean enableMonitor = true;
        private int defaultRecommendCount = 10;
        private int maxRecommendCount = 100;
        private long cacheExpireTime = 300; // 5分钟
        private String defaultAlgorithm = "hybrid";

        // Getters and Setters
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }

        public boolean isEnableCache() { return enableCache; }
        public void setEnableCache(boolean enableCache) { this.enableCache = enableCache; }

        public boolean isEnableMonitor() { return enableMonitor; }
        public void setEnableMonitor(boolean enableMonitor) { this.enableMonitor = enableMonitor; }

        public int getDefaultRecommendCount() { return defaultRecommendCount; }
        public void setDefaultRecommendCount(int defaultRecommendCount) { this.defaultRecommendCount = defaultRecommendCount; }

        public int getMaxRecommendCount() { return maxRecommendCount; }
        public void setMaxRecommendCount(int maxRecommendCount) { this.maxRecommendCount = maxRecommendCount; }

        public long getCacheExpireTime() { return cacheExpireTime; }
        public void setCacheExpireTime(long cacheExpireTime) { this.cacheExpireTime = cacheExpireTime; }

        public String getDefaultAlgorithm() { return defaultAlgorithm; }
        public void setDefaultAlgorithm(String defaultAlgorithm) { this.defaultAlgorithm = defaultAlgorithm; }
    }
} 