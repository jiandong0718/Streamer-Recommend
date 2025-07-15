package com.recommend.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class SystemResourceMonitor {
    
    private final OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    private final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    private final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    
    private final AtomicLong lastCpuTime = new AtomicLong(0);
    private final AtomicLong lastUpTime = new AtomicLong(0);
    
    /**
     * 获取系统负载
     */
    public double getSystemLoad() {
        try {
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                return ((com.sun.management.OperatingSystemMXBean) osBean).getSystemLoadAverage();
            }
            return osBean.getSystemLoadAverage();
        } catch (Exception e) {
            log.error("Error getting system load", e);
            return 0.0;
        }
    }
    
    /**
     * 获取CPU使用率
     */
    public double getCpuUsage() {
        try {
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                return ((com.sun.management.OperatingSystemMXBean) osBean).getProcessCpuLoad();
            }
            
            // 计算CPU使用率
            long currentCpuTime = threadBean.getCurrentThreadCpuTime();
            long currentUpTime = System.nanoTime();
            
            long lastCpu = lastCpuTime.getAndSet(currentCpuTime);
            long lastUp = lastUpTime.getAndSet(currentUpTime);
            
            if (lastCpu == 0 || lastUp == 0) {
                return 0.0;
            }
            
            long cpuTimeDiff = currentCpuTime - lastCpu;
            long upTimeDiff = currentUpTime - lastUp;
            
            return (double) cpuTimeDiff / upTimeDiff;
        } catch (Exception e) {
            log.error("Error getting CPU usage", e);
            return 0.0;
        }
    }
    
    /**
     * 获取内存使用率
     */
    public double getMemoryUsage() {
        try {
            long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
            long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
            
            return (double) usedMemory / maxMemory;
        } catch (Exception e) {
            log.error("Error getting memory usage", e);
            return 0.0;
        }
    }
    
    /**
     * 获取线程数
     */
    public int getThreadCount() {
        try {
            return threadBean.getThreadCount();
        } catch (Exception e) {
            log.error("Error getting thread count", e);
            return 0;
        }
    }
    
    /**
     * 获取死锁线程数
     */
    public int getDeadlockedThreadCount() {
        try {
            long[] deadlockedThreads = threadBean.findDeadlockedThreads();
            return deadlockedThreads != null ? deadlockedThreads.length : 0;
        } catch (Exception e) {
            log.error("Error getting deadlocked thread count", e);
            return 0;
        }
    }
    
    /**
     * 获取系统资源使用情况
     */
    public SystemResourceUsage getSystemResourceUsage() {
        return SystemResourceUsage.builder()
            .systemLoad(getSystemLoad())
            .cpuUsage(getCpuUsage())
            .memoryUsage(getMemoryUsage())
            .threadCount(getThreadCount())
            .deadlockedThreadCount(getDeadlockedThreadCount())
            .timestamp(System.currentTimeMillis())
            .build();
    }
    
    /**
     * 系统资源使用情况
     */
    @lombok.Data
    @lombok.Builder
    public static class SystemResourceUsage {
        private double systemLoad;
        private double cpuUsage;
        private double memoryUsage;
        private int threadCount;
        private int deadlockedThreadCount;
        private long timestamp;
        
        @Override
        public String toString() {
            return String.format(
                "System Resource Usage:\n" +
                "- System Load: %.2f\n" +
                "- CPU Usage: %.2f%%\n" +
                "- Memory Usage: %.2f%%\n" +
                "- Thread Count: %d\n" +
                "- Deadlocked Threads: %d\n" +
                "- Timestamp: %d",
                systemLoad,
                cpuUsage * 100,
                memoryUsage * 100,
                threadCount,
                deadlockedThreadCount,
                timestamp
            );
        }
    }
} 