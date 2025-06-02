package com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType;

import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import lombok.*;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartyMemberInfo {
    private String gameName;
    private String tagLine;
    private String profileUrl;
    private RankInfoDto rankInfo; // 티어, 랭크, 리그포인트, 승패
    private List<String> most3Champ;
    private PlayPosition myPosition;
}