package com.tools.seoultech.timoproject.riot.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RankInfoDto {
    private String tier;   // GOLD, PLATINUM, MASTER 등
    private String rank;   // I, II, III, IV (단 마스터 이상은 없음)
    private int lp;        // League Points
    private int wins;
    private int losses;
}
