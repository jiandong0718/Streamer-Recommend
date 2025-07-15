package com.recommend.service.impl;

import com.recommend.service.CategoryService;
import com.recommend.service.mapper.CategoryMapper;
import com.recommend.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryMapper.selectById(categoryId);
    }
    
    @Override
    public List<Category> getCategoryList() {
        return categoryMapper.selectList(null);
    }
    
    @Override
    public List<Category> getCategoriesByType(String type) {
        return categoryMapper.selectByType(type);
    }
    
    @Override
    public List<Category> getCategoriesByStatus(Integer status) {
        return categoryMapper.selectByStatus(status);
    }
    
    @Override
    public List<Category> searchCategories(String keyword) {
        return categoryMapper.searchCategories(keyword);
    }
    
    @Override
    public List<Category> getHotCategories(Integer limit) {
        return categoryMapper.selectHotCategories(limit);
    }
    
    @Override
    @Transactional
    public void createCategory(Category category) {
        categoryMapper.insert(category);
        log.info("创建分类成功，ID: {}", category.getId());
    }
    
    @Override
    @Transactional
    public void updateCategory(Category category) {
        categoryMapper.updateById(category);
        log.info("更新分类成功，ID: {}", category.getId());
    }
    
    @Override
    @Transactional
    public void updateCategoryStatus(Long categoryId, Integer status) {
        Category category = new Category();
        category.setId(categoryId);
        category.setStatus(status);
        categoryMapper.updateById(category);
        log.info("更新分类状态成功，ID: {}, 状态: {}", categoryId, status);
    }
    
    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryMapper.deleteById(categoryId);
        log.info("删除分类成功，ID: {}", categoryId);
    }
} 