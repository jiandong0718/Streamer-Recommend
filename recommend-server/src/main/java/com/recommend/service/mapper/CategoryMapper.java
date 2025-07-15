package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    
    /**
     * 根据类型查询分类列表
     */
    List<Category> selectByType(@Param("type") String type);
    
    /**
     * 根据状态查询分类列表
     */
    List<Category> selectByStatus(@Param("status") Integer status);
    
    /**
     * 搜索分类
     */
    List<Category> searchCategories(@Param("keyword") String keyword);
    
    /**
     * 获取热门分类
     */
    List<Category> selectHotCategories(@Param("limit") Integer limit);
} 