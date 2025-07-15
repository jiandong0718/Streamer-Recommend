package com.recommend.common.exception;

public class AlgorithmException extends RuntimeException {
    
    public AlgorithmException(String message) {
        super(message);
    }
    
    public AlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static AlgorithmException of(String message) {
        return new AlgorithmException(message);
    }
    
    public static AlgorithmException of(String message, Throwable cause) {
        return new AlgorithmException(message, cause);
    }
} 