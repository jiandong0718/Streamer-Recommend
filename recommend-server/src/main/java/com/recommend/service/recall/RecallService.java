package com.recommend.service.recall;

import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.UserProfile;
import com.recommend.service.UserProfileService;
import com.recommend.service.mapper.GameMasterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
@Slf4j
public class RecallService {
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Autowired
    private GameMasterMapper gameMasterMapper;
    
    public List<GameMaster> recallCandidates(Long userId) {
        // 1. 获取用户画像
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        
        // 2. 多路召回

        // 2.1 基于标签的召回
        List<GameMaster> tagBasedCandidates = recallByTags(userProfile);
        List<GameMaster> candidates = new ArrayList<>(tagBasedCandidates);
        
        // 2.2 基于相似用户的召回
        List<GameMaster> similarUserCandidates = recallBySimilarUsers(userId);
        candidates.addAll(similarUserCandidates);
        
        // 2.3 基于热门度的召回
        List<GameMaster> popularCandidates = recallByPopularity();
        candidates.addAll(popularCandidates);
        
        // 3. 去重
        return candidates.stream()
            .distinct()
            .collect(Collectors.toList());
    }
    
    private List<GameMaster> recallByTags(UserProfile userProfile) {
        // 从用户画像中获取游戏类型偏好
        String gameTypes = userProfile.getGameTypes();
        if (gameTypes == null || gameTypes.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 将游戏类型字符串转换为标签列表
        List<String> tags = Arrays.asList(gameTypes.split(","));
        return gameMasterMapper.findByTags(tags);
    }
    
    private List<GameMaster> recallBySimilarUsers(Long userId) {
        // 实现基于相似用户的召回逻辑
        return new ArrayList<>();
    }
    
    private List<GameMaster> recallByPopularity() {
        // 实现基于热门度的召回逻辑
        return new ArrayList<>();
    }
} 