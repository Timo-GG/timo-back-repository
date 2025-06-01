package com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.*;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CertifiedMemberInfo extends CompactMemberInfo{
    private String univName;
    private String department;
    private Gender gender;
    private String mbti;

    public CertifiedMemberInfo(String univName, String department, Gender gender, String mbti,
                               String puuid, String gameName, String tagLine, String profileUrl, String tier, String rank, List<String> most3Champ, PlayPosition myPosition) {
        super(puuid, gameName, tagLine,profileUrl, tier, rank, most3Champ, myPosition);
        this.univName = univName;
        this.department = department;
        this.gender = gender;
        this.mbti = mbti;

    }
}
