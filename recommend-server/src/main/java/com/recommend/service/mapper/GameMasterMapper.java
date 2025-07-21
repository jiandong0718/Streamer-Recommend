package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.GameMaster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GameMasterMapper extends BaseMapper<GameMaster> {
    
    /**
     * 搜索陪玩
     */
    List<GameMaster> searchGameMasters(@Param("keyword") String keyword, @Param("status") Integer status);
    
    /**
     * 根据地区查询陪玩列表
     */
    List<GameMaster> selectByRegion(@Param("region") String region);
    
    /**
     * 根据状态查询陪玩列表
     */
    List<GameMaster> selectByStatus(@Param("status") Integer status);
    
    /**
     * 根据评分范围查询陪玩列表
     */
    List<GameMaster> selectByScoreRange(@Param("minScore") Double minScore, @Param("maxScore") Double maxScore);
    
    /**
     * 根据价格范围查询陪玩列表
     */
    List<GameMaster> selectByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    /**
     * 根据标签查询陪玩列表
     */
    List<GameMaster> findByTags(@Param("tags") List<String> tags);
    
    /**
     * 根据游戏ID查询陪玩师列表
     */
    List<GameMaster> selectByGameId(@Param("gameId") Long gameId);
    
    /**
     * 根据游戏类型查询陪玩师列表
     */
    List<GameMaster> selectByGameTypes(@Param("gameTypes") String gameTypes);
} 