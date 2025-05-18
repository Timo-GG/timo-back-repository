package com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType;

import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import lombok.Getter;

import java.util.List;


@Getter
public class CertifiedMemberInfo extends CompactMemberInfo{
    private String univName;

    public CertifiedMemberInfo(String univName, RiotAccount riotAccount, RankInfoDto rankInfo, List<String> most3Champ) {
        super(riotAccount, rankInfo, most3Champ);
        this.univName = univName;
    }
}
