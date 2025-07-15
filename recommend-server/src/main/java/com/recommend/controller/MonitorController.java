package com.recommend.controller;

import com.recommend.monitor.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
@Slf4j
public class MonitorController {
    
    private final RecommendMonitorService monitorService;
    private final RecommendVisualizationService visualizationService;
    private final RecommendAlertService alertService;
    
    /**
     * 获取系统健康状态
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealthStatus() {
        try {
            Map<String, Object> healthReport = visualizationService.generateHealthReport();
            return ResponseEntity.ok(healthReport);
        } catch (Exception e) {
            log.error("Error getting health status", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 获取性能指标图表数据
     */
    @GetMapping("/metrics/chart")
    public ResponseEntity<Map<String, Object>> getMetricsChart(
        @RequestParam String type,
        @RequestParam(defaultValue = "24") int hours
    ) {
        try {
            Map<String, Object> chartData = visualizationService.generatePerformanceChartData(type, hours);
            return ResponseEntity.ok(chartData);
        } catch (Exception e) {
            log.error("Error getting metrics chart data", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 获取系统资源使用图表数据
     */
    @GetMapping("/resources/chart")
    public ResponseEntity<Map<String, Object>> getResourceChart(
        @RequestParam(defaultValue = "24") int hours
    ) {
        try {
            Map<String, Object> chartData = visualizationService.generateResourceChartData(hours);
            return ResponseEntity.ok(chartData);
        } catch (Exception e) {
            log.error("Error getting resource chart data", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 获取告警统计图表数据
     */
    @GetMapping("/alerts/chart")
    public ResponseEntity<Map<String, Object>> getAlertChart(
        @RequestParam(defaultValue = "24") int hours
    ) {
        try {
            Map<String, Object> chartData = visualizationService.generateAlertChartData(hours);
            return ResponseEntity.ok(chartData);
        } catch (Exception e) {
            log.error("Error getting alert chart data", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 获取最近的告警列表
     */
    @GetMapping("/alerts")
    public ResponseEntity<Map<String, Object>> getRecentAlerts(
        @RequestParam(defaultValue = "10") int count
    ) {
        try {
            Map<String, Object> alerts = monitorService.getRecentAlerts(count);
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            log.error("Error getting recent alerts", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 获取系统资源使用情况
     */
    @GetMapping("/resources")
    public ResponseEntity<Map<String, Object>> getSystemResources() {
        try {
            Map<String, Object> resources = monitorService.getSystemResources();
            return ResponseEntity.ok(resources);
        } catch (Exception e) {
            log.error("Error getting system resources", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 获取推荐系统性能指标
     */
    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics(
        @RequestParam(defaultValue = "latest") String type
    ) {
        try {
            Map<String, Object> metrics = monitorService.getMetrics(type);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            log.error("Error getting metrics", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 手动触发指标收集
     */
    @PostMapping("/metrics/collect")
    public ResponseEntity<Void> collectMetrics() {
        try {
            monitorService.collectMetrics();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error collecting metrics", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 手动触发告警检查
     */
    @PostMapping("/alerts/check")
    public ResponseEntity<Void> checkAlerts() {
        try {
            alertService.checkAlerts();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error checking alerts", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 手动触发数据清理
     */
    @PostMapping("/data/cleanup")
    public ResponseEntity<Void> cleanupData() {
        try {
            monitorService.cleanupData();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error cleaning up data", e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 