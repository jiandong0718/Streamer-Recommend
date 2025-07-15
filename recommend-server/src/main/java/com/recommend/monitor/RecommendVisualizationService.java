package com.recommend.monitor;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendVisualizationService {
    
    private final RecommendMetricsCollector metricsCollector;
    private final RecommendDataStorageService storageService;
    
    /**
     * 生成健康报告
     */
    public Map<String, Object> generateHealthReport() {
        Map<String, Object> report = new HashMap<>();
        
        try {
            RecommendMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
            
            report.put("timestamp", snapshot.getTimestamp());
            report.put("healthScore", snapshot.calculateHealthScore());
            report.put("status", getHealthStatus(snapshot.calculateHealthScore()));
            
            // 核心指标
            Map<String, Object> coreMetrics = new HashMap<>();
            coreMetrics.put("totalRecommendations", snapshot.getTotalRecommendations());
            coreMetrics.put("averageResponseTime", snapshot.getAverageResponseTime());
            coreMetrics.put("errorRate", snapshot.getErrorRate());
            coreMetrics.put("cacheHitRate", snapshot.getCacheHitRate());
            report.put("coreMetrics", coreMetrics);
            
            // 系统资源
            Map<String, Object> systemResources = new HashMap<>();
            systemResources.put("cpuUsage", snapshot.getCpuUsage());
            systemResources.put("memoryUsage", snapshot.getMemoryUsage());
            systemResources.put("diskUsage", snapshot.getDiskUsage());
            systemResources.put("networkUsage", snapshot.getNetworkUsage());
            report.put("systemResources", systemResources);
            
            // 告警信息
            Map<String, Object> alerts = new HashMap<>();
            alerts.put("totalAlerts", snapshot.getAlertCount());
            alerts.put("criticalAlerts", snapshot.getCriticalAlertCount());
            alerts.put("warningAlerts", snapshot.getWarningAlertCount());
            report.put("alerts", alerts);
            
        } catch (Exception e) {
            log.error("Error generating health report", e);
            report.put("error", "Failed to generate health report");
        }
        
        return report;
    }
    
    /**
     * 生成性能图表数据
     */
    public Map<String, Object> generatePerformanceChartData(String type, int hours) {
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            // 模拟时间序列数据
            List<Map<String, Object>> timeSeriesData = new ArrayList<>();
            
            for (int i = 0; i < hours; i++) {
                Map<String, Object> dataPoint = new HashMap<>();
                dataPoint.put("timestamp", System.currentTimeMillis() - (hours - i) * 3600000L);
                
                switch (type) {
                    case "response_time":
                        dataPoint.put("value", 500 + Math.random() * 200);
                        break;
                    case "error_rate":
                        dataPoint.put("value", Math.random() * 0.05);
                        break;
                    case "cache_hit_rate":
                        dataPoint.put("value", 0.7 + Math.random() * 0.3);
                        break;
                    case "throughput":
                        dataPoint.put("value", 1000 + Math.random() * 500);
                        break;
                    default:
                        dataPoint.put("value", Math.random() * 100);
                }
                
                timeSeriesData.add(dataPoint);
            }
            
            chartData.put("type", type);
            chartData.put("data", timeSeriesData);
            chartData.put("unit", getMetricUnit(type));
            chartData.put("title", getMetricTitle(type));
            
        } catch (Exception e) {
            log.error("Error generating performance chart data", e);
            chartData.put("error", "Failed to generate chart data");
        }
        
        return chartData;
    }
    
    /**
     * 生成资源使用图表数据
     */
    public Map<String, Object> generateResourceChartData(int hours) {
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            List<Map<String, Object>> cpuData = new ArrayList<>();
            List<Map<String, Object>> memoryData = new ArrayList<>();
            List<Map<String, Object>> diskData = new ArrayList<>();
            List<Map<String, Object>> networkData = new ArrayList<>();
            
            for (int i = 0; i < hours; i++) {
                long timestamp = System.currentTimeMillis() - (hours - i) * 3600000L;
                
                Map<String, Object> cpuPoint = new HashMap<>();
                cpuPoint.put("timestamp", timestamp);
                cpuPoint.put("value", 0.3 + Math.random() * 0.4);
                cpuData.add(cpuPoint);
                
                Map<String, Object> memoryPoint = new HashMap<>();
                memoryPoint.put("timestamp", timestamp);
                memoryPoint.put("value", 0.5 + Math.random() * 0.3);
                memoryData.add(memoryPoint);
                
                Map<String, Object> diskPoint = new HashMap<>();
                diskPoint.put("timestamp", timestamp);
                diskPoint.put("value", 0.2 + Math.random() * 0.2);
                diskData.add(diskPoint);
                
                Map<String, Object> networkPoint = new HashMap<>();
                networkPoint.put("timestamp", timestamp);
                networkPoint.put("value", 0.1 + Math.random() * 0.3);
                networkData.add(networkPoint);
            }
            
            chartData.put("cpu", cpuData);
            chartData.put("memory", memoryData);
            chartData.put("disk", diskData);
            chartData.put("network", networkData);
            
        } catch (Exception e) {
            log.error("Error generating resource chart data", e);
            chartData.put("error", "Failed to generate resource chart data");
        }
        
        return chartData;
    }
    
    /**
     * 生成告警图表数据
     */
    public Map<String, Object> generateAlertChartData(int hours) {
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            List<Map<String, Object>> alertData = new ArrayList<>();
            
            for (int i = 0; i < hours; i++) {
                Map<String, Object> dataPoint = new HashMap<>();
                dataPoint.put("timestamp", System.currentTimeMillis() - (hours - i) * 3600000L);
                dataPoint.put("critical", (int) (Math.random() * 3));
                dataPoint.put("warning", (int) (Math.random() * 10));
                dataPoint.put("info", (int) (Math.random() * 20));
                alertData.add(dataPoint);
            }
            
            chartData.put("data", alertData);
            chartData.put("title", "Alert Distribution");
            
        } catch (Exception e) {
            log.error("Error generating alert chart data", e);
            chartData.put("error", "Failed to generate alert chart data");
        }
        
        return chartData;
    }
    
    private String getHealthStatus(Double healthScore) {
        if (healthScore >= 90) {
            return "excellent";
        } else if (healthScore >= 70) {
            return "good";
        } else if (healthScore >= 50) {
            return "warning";
        } else {
            return "critical";
        }
    }
    
    private String getMetricUnit(String type) {
        switch (type) {
            case "response_time":
                return "ms";
            case "error_rate":
                return "%";
            case "cache_hit_rate":
                return "%";
            case "throughput":
                return "req/s";
            default:
                return "";
        }
    }
    
    private String getMetricTitle(String type) {
        switch (type) {
            case "response_time":
                return "Average Response Time";
            case "error_rate":
                return "Error Rate";
            case "cache_hit_rate":
                return "Cache Hit Rate";
            case "throughput":
                return "Throughput";
            default:
                return "Unknown Metric";
        }
    }
} 