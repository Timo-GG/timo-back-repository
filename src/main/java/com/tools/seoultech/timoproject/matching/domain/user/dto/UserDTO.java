package com.tools.seoultech.timoproject.matching.domain.user.dto;


import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.DuoInfo_Ver2;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.UserInfo_Ver2;

import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.RiotAccount;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class UserDTO{
    // 중첩 Record 필드. [Request Duo•Colosseum], [Response Duo•Colosseum]
    @Builder
    public record RequestDuo(
            Long memberId,
            RiotAccount riotAccount,
            UserInfo_Ver2 userInfo,
            DuoInfo_Ver2 duoInfo
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
            RiotAccount riotAccount,
            UserInfo_Ver2 userInfo,
            DuoInfo_Ver2 duoInfo
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
