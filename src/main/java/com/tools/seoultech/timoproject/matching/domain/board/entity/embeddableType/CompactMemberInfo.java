package com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import lombok.*;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompactMemberInfo {
    /** RiotAccount */
    private String puuid;
    private String gameName;
    private String tagLine;
    private String profileUrl;

    /** RankInfoDto*/
    private String tier;   // GOLD, PLATINUM, MASTER 등
    private String rank;

    /** 나머지 */
    private List<String> most3Champ;
    private PlayPosition myPosition;
}
