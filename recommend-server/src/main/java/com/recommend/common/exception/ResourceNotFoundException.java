package com.recommend.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static ResourceNotFoundException of(String resourceType, Object id) {
        return new ResourceNotFoundException(String.format("%s with id %s not found", resourceType, id));
    }
    
    public static ResourceNotFoundException of(String message) {
        return new ResourceNotFoundException(message);
    }
} 