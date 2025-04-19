package com.tools.seoultech.timoproject.riot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class RankInfoDto {
    private String tier;   // GOLD, PLATINUM, MASTER 등
    private String rank;   // I, II, III, IV (단 마스터 이상은 없음)
    private int lp;        // League Points
    private int wins;
    private int losses;
}
