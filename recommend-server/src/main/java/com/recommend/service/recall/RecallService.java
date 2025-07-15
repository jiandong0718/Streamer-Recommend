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
        List<GameMaster> candidates = new ArrayList<>();
        
        // 2.1 基于标签的召回
        List<GameMaster> tagBasedCandidates = recallByTags(userProfile.getTags());
        candidates.addAll(tagBasedCandidates);
        
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
    
    private List<GameMaster> recallByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
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