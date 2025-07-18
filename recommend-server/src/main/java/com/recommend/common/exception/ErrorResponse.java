package com.recommend.common.exception;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    private String code;
    private String message;
    private Object data;
    private Long timestamp;
    
    public static ErrorResponse of(String code, String message) {
        return ErrorResponse.builder()
            .code(code)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .build();
    }
    
    public static ErrorResponse of(String code, String message, Object data) {
        return ErrorResponse.builder()
            .code(code)
            .message(message)
            .data(data)
            .timestamp(System.currentTimeMillis())
            .build();
    }
} 