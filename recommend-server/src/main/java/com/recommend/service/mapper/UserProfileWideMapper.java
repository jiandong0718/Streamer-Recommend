package com.recommend.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recommend.common.entity.UserProfileWide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserProfileWideMapper extends BaseMapper<UserProfileWide> {
    
    /**
     * 根据用户ID查询宽表数据
     */
    UserProfileWide selectByUserId(@Param("userId") Long userId);
    
    /**
     * 批量更新用户特征
     */
    void batchUpdateFeatures(@Param("userId") Long userId, @Param("features") String features);
    
    /**
     * 更新用户标签特征
     */
    void updateTagFeatures(@Param("userId") Long userId, @Param("tagFeatures") String tagFeatures);
    
    /**
     * 更新用户偏好特征
     */
    void updatePreferenceFeatures(@Param("userId") Long userId, @Param("preferenceFeatures") String preferenceFeatures);
} 