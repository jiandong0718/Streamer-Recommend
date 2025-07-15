package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    
    /**
     * 搜索标签
     */
    List<Tag> searchTags(@Param("keyword") String keyword, @Param("type") Integer type, @Param("category") String category);
    
    /**
     * 根据类型和分类查询标签列表
     */
    List<Tag> selectByTypeAndCategory(@Param("type") Integer type, @Param("category") String category);
    
    /**
     * 根据状态查询标签列表
     */
    List<Tag> selectByStatus(@Param("status") Integer status);
    
    /**
     * 根据权重范围查询标签列表
     */
    List<Tag> selectByWeightRange(@Param("minWeight") Double minWeight, @Param("maxWeight") Double maxWeight);
} 