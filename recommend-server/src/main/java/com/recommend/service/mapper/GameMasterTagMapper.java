package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.GameMasterTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GameMasterTagMapper extends BaseMapper<GameMasterTag> {
    
    /**
     * 根据陪玩ID查询标签列表
     */
    List<GameMasterTag> selectByMasterId(@Param("masterId") Long masterId);
    
    /**
     * 根据标签ID查询陪玩列表
     */
    List<GameMasterTag> selectByTagId(@Param("tagId") Long tagId);
    
    /**
     * 根据权重范围查询陪玩标签列表
     */
    List<GameMasterTag> selectByWeightRange(@Param("minWeight") Double minWeight, @Param("maxWeight") Double maxWeight);
    
    /**
     * 更新陪玩标签权重
     */
    void updateWeight(@Param("masterId") Long masterId, @Param("tagId") Long tagId, @Param("weight") Double weight);
    
    /**
     * 根据陪玩ID和标签ID删除关联
     */
    void deleteByMasterIdAndTagId(@Param("masterId") Long masterId, @Param("tagId") Long tagId);
    
    /**
     * 根据陪玩ID删除所有标签关联
     */
    void deleteByMasterId(@Param("masterId") Long masterId);
} 