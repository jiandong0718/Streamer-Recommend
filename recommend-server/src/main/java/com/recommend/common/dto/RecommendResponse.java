package com.recommend.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * 推荐响应DTO
 * 
 * @author liujiandong
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendResponse {

    /**
     * 是否成功
     */
    private Boolean success = true;

    /**
     * 响应消息
     */
    private String message = "成功";

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer rows;

    /**
     * 总数
     */
    private Long total;

    /**
     * 扩展信息
     */
    private Map<String, Object> extra;

    /**
     * 推荐场景
     */
    private String scene;

    /**
     * 推荐算法
     */
    private String algorithm;

    /**
     * 响应时间戳
     */
    private Long timestamp = System.currentTimeMillis();

    /**
     * 成功响应
     */
    public static RecommendResponse success(Object data) {
        RecommendResponse response = new RecommendResponse();
        response.setSuccess(true);
        response.setMessage("成功");
        response.setData(data);
        return response;
    }

    /**
     * 成功响应（带消息）
     */
    public static RecommendResponse success(String message, Object data) {
        RecommendResponse response = new RecommendResponse();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    /**
     * 错误响应
     */
    public static RecommendResponse error(String message) {
        RecommendResponse response = new RecommendResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    /**
     * 错误响应（带数据）
     */
    public static RecommendResponse error(String message, Object data) {
        RecommendResponse response = new RecommendResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    /**
     * 分页响应
     */
    public static RecommendResponse page(Object data, Integer page, Integer rows, Long total) {
        RecommendResponse response = new RecommendResponse();
        response.setSuccess(true);
        response.setMessage("成功");
        response.setData(data);
        response.setPage(page);
        response.setRows(rows);
        response.setTotal(total);
        return response;
    }
} 