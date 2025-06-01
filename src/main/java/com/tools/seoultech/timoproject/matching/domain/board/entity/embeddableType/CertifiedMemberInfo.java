package com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType;

import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.*;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import lombok.Getter;

import java.util.List;


@Getter
public class CertifiedMemberInfo extends CompactMemberInfo{
    private String univName;
    private String department;
    private Gender gender;
    private String mbti;
    private String profileUrl;

    public CertifiedMemberInfo(String univName, String department, Gender gender, String mbti, String profileUrl,
                               String puuid, String gameName, String tagLine, String tier, String rank, List<String> most3Champ, PlayPosition myPosition) {
        super(puuid, gameName, tagLine, tier, rank, most3Champ, myPosition);
        this.univName = univName;
        this.department = department;
        this.gender = gender;
        this.mbti = mbti;
        this.profileUrl = profileUrl;

    }
}
