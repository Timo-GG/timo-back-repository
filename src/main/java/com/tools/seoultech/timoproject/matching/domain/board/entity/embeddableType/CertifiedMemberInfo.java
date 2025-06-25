package com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType;

import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.Gender;
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
    private String verificationType;

    public CertifiedMemberInfo(String univName, String department, Gender gender, String mbti,
                               RiotAccount riotAccount, RankInfoDto rankInfo, List<String> most3Champ) {
        super(riotAccount, rankInfo, most3Champ);
        this.univName = univName;
        this.department = department;
        this.gender = gender;
        this.mbti = mbti;
        this.verificationType = riotAccount.getVerificationType() != null ?
                riotAccount.getVerificationType().name() : null;

    }
}
