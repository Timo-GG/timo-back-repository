package com.tools.seoultech.timoproject.matching.domain.user.dto;


import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.UserInfo;

import com.tools.seoultech.timoproject.matching.domain.user.entity.enumType.Gender;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.RiotAccount;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public abstract class UserDTO{
    // 중첩 Record 필드. [Request Duo•Colosseum], [Response Duo•Colosseum]
    @Builder
    public record RequestDuo(
            Long memberId,
            CertifiedUnivInfo certifiedUnivInfo,
            Gender gender,
            String mbti,
            RiotAccount riotAccount,
            CompactPlayerHistory compactPlayerHistory,
            UserInfo userInfo,
            DuoInfo duoInfo
    )implements Request {}

    @Builder
    public record RequestColosseum(
            Long memberId,
            RiotAccount riotAccount,
            List<RiotAccount> partyMemberRiotAccountList
    )implements Request {}

    @Builder
    public record ResponseDuo(
            UUID userUUID,
            CertifiedUnivInfo certifiedUnivInfo,
            Gender gender,
            String mbti,
            RiotAccount riotAccount,
            CompactPlayerHistory compactPlayerHistory,
            UserInfo userInfo,
            DuoInfo duoInfo
    )implements Response {}

    @Builder
    public record ResponseColosseum(
            UUID userUUID,
            RiotAccount riotAccount,
            List<PartyMemberInfo> partyMemberInfoList
    )implements Response {}

    // 인터페이스 태그
    public interface Request extends BaseInterface {}
    public interface Response extends BaseInterface {}
    public interface BaseInterface {
    }
}
