package com.recommend.common.request;

import lombok.Data;

@Data
public class FeedbackRequest {
    private Long userId;
    private Long masterId;
    private Integer rating;
    private String feedback;
} 