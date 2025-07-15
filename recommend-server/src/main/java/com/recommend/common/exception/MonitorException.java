package com.recommend.common.exception;

public class MonitorException extends RuntimeException {
    
    public MonitorException(String message) {
        super(message);
    }
    
    public MonitorException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static MonitorException of(String message) {
        return new MonitorException(message);
    }
    
    public static MonitorException of(String message, Throwable cause) {
        return new MonitorException(message, cause);
    }
} 