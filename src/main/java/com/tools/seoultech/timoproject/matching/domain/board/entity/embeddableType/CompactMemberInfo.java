package com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType;

import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import lombok.*;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompactMemberInfo {
    private RiotAccount riotAccount;
    private RankInfoDto rankInfo;

    private List<String> most3Champ;
}
