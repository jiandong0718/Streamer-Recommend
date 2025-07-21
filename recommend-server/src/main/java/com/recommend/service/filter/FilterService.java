package com.recommend.service.filter;

import com.recommend.common.entity.GameMaster;
import com.recommend.common.entity.UserProfile;
import com.recommend.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilterService {

    @Autowired
    private UserProfileService userProfileService;

    public List<GameMaster> filterAndRerank(List<GameMaster> candidates, Long userId) {
        // 1. 获取用户画像
        UserProfile userProfile = userProfileService.getUserProfile(userId);

        // 2. 应用过滤规则
        List<GameMaster> filtered = candidates.stream()
                .filter(master -> applyFilterRules(master, userProfile))
                .collect(Collectors.toList());

        // 3. 应用多样性规则
        List<GameMaster> diversified = applyDiversityRules(filtered);

        return diversified;
    }

    private boolean applyFilterRules(GameMaster master, UserProfile userProfile) {
        // 1. 基础过滤规则
        if (!isValidMaster(master)) {
            return false;
        }

        // 2. 用户偏好过滤
        if (!matchesUserPreferences(master, userProfile)) {
            return false;
        }

        // 3. 业务规则过滤
        if (!satisfiesBusinessRules(master)) {
            return false;
        }

        return true;
    }

    private boolean isValidMaster(GameMaster master) {
        // 实现基础有效性检查
        return master != null && master.getId() != null;
    }

    private boolean matchesUserPreferences(GameMaster master, UserProfile userProfile) {
        // 1. 游戏类型匹配
        if (userProfile.getGameTypes() != null && !userProfile.getGameTypes().isEmpty()) {
            boolean gameTypeMatched = master.getGames().stream()
                    .anyMatch(game -> userProfile.getGameTypes().contains(game.getGameType()));
            if (!gameTypeMatched) {
                return false;
            }
        }

        // 2. 标签匹配
        if (userProfile.getTags() != null && !userProfile.getTags().isEmpty()) {
            List<String> userTags = Arrays.asList(userProfile.getTags().split(","));
            boolean tagMatched = Arrays.stream(master.getTags().split(","))
                    .anyMatch(userTags::contains);
            if (!tagMatched) {
                return false;
            }
        }

        return true;
    }

    private boolean satisfiesBusinessRules(GameMaster master) {
        // 1. 评分检查（>=4.0）
        if (master.getScore() == null || master.getScore().compareTo(new BigDecimal("4.0")) < 0) {
            return false;
        }

        // 2. 订单量检查（>=10）
        if (master.getOrderCount() == null || master.getOrderCount() < 10) {
            return false;
        }

        // 3. 在线状态检查（status=1表示在线）
        if (master.getStatus() == null || master.getStatus() != 1) {
            return false;
        }

        return true;
    }

    private List<GameMaster> applyDiversityRules(List<GameMaster> candidates) {
        if (candidates.isEmpty()) {
            return candidates;
        }

        // 按游戏类型分组
        Map<String, List<GameMaster>> groupedByGameType = candidates.stream()
                .collect(Collectors.groupingBy(
                        master -> master.getGames().stream()
                                .map(game -> game.getGameType())
                                .collect(Collectors.joining(","))
                ));

        // 轮询选择不同游戏类型的陪玩
        List<GameMaster> diversified = new ArrayList<>();
        while (!groupedByGameType.isEmpty()) {
            for (Iterator<Map.Entry<String, List<GameMaster>>> it = groupedByGameType.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, List<GameMaster>> entry = it.next();
                diversified.add(entry.getValue().remove(0));
                if (entry.getValue().isEmpty()) {
                    it.remove();
                }
            }
        }

        return diversified;
    }
} 