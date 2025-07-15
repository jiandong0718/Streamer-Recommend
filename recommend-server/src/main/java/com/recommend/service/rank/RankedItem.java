package com.recommend.service.rank;

import com.recommend.common.entity.GameMaster;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankedItem {
    
    private GameMaster master;
    private Double score;
    private String reason;
    private Integer rank;
    
    public RankedItem(GameMaster master, Double score) {
        this.master = master;
        this.score = score;
    }
} 