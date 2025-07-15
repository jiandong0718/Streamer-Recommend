package com.recommend.service;

import com.recommend.common.entity.Category;
import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {
    
    /**
     * 根据ID获取分类信息
     */
    Category getCategoryById(Long categoryId);
    
    /**
     * 获取分类列表
     */
    List<Category> getCategoryList();
    
    /**
     * 根据类型获取分类列表
     */
    List<Category> getCategoriesByType(String type);
    
    /**
     * 根据状态获取分类列表
     */
    List<Category> getCategoriesByStatus(Integer status);
    
    /**
     * 搜索分类
     */
    List<Category> searchCategories(String keyword);
    
    /**
     * 获取热门分类
     */
    List<Category> getHotCategories(Integer limit);
    
    /**
     * 创建分类
     */
    void createCategory(Category category);
    
    /**
     * 更新分类信息
     */
    void updateCategory(Category category);
    
    /**
     * 更新分类状态
     */
    void updateCategoryStatus(Long categoryId, Integer status);
    
    /**
     * 删除分类
     */
    void deleteCategory(Long categoryId);
} 