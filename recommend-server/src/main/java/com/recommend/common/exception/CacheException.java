package com.recommend.common.exception;

public class CacheException extends RuntimeException {
    
    public CacheException(String message) {
        super(message);
    }
    
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static CacheException of(String message) {
        return new CacheException(message);
    }
    
    public static CacheException of(String message, Throwable cause) {
        return new CacheException(message, cause);
    }
} 