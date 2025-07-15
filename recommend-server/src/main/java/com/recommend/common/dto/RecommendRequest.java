package com.recommend.common.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import java.util.List;
import java.util.Map;

/**
 * 推荐请求DTO
 * 
 * @author liujiandong
 */
@Data
public class RecommendRequest {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类类型
     */
    private String category;

    /**
     * 语言代码
     */
    private String locale;

    /**
     * 国家代码
     */
    private String countryIsoCode;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 地区
     */
    private String region;

    /**
     * 最小等级
     */
    private Integer minLevel;

    /**
     * 最大等级
     */
    private Integer maxLevel;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 用户偏好
     */
    private String preferences;

    /**
     * 页码
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer page = 1;

    /**
     * 每页数量
     */
    @Min(value = 1, message = "每页数量必须大于0")
    @Max(value = 100, message = "每页数量不能超过100")
    private Integer limit = 10;

    /**
     * 兼容旧字段
     */
    private Integer rows = 10;

    /**
     * 推荐场景
     */
    private String scene;

    /**
     * 调试模式
     */
    private boolean debug = false;

    /**
     * 摘要信息
     */
    private String summary;

    /**
     * 用户偏好标签
     */
    private List<String> preferTags;

    /**
     * 性别偏好
     */
    private String preferGender;

    /**
     * 价格范围
     */
    private PriceRange priceRange;

    /**
     * 技能要求
     */
    private List<String> skillRequirements;

    /**
     * 排序方式
     */
    private String sortBy;

    /**
     * 筛选条件
     */
    private Map<String, Object> filters;

    /**
     * 扩展参数
     */
    private Map<String, Object> extendParams;

    /**
     * 价格范围内部类
     */
    @Data
    public static class PriceRange {
        private Double minPrice;
        private Double maxPrice;
    }
} 