package com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType;

import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
public class PartyMemberInfo extends CompactMemberInfo {
    private PlayPosition myPosition;

    public PartyMemberInfo(PlayPosition myPosition, RiotAccount riotAccount, RankInfoDto rankInfo, List<String> most3Champ) {
        super(riotAccount, rankInfo, most3Champ);
        this.myPosition = myPosition;
    }
}