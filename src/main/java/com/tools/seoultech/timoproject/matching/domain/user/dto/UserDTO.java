package com.tools.seoultech.timoproject.matching.domain.user.dto;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.DuoInfo_Ver2;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.UserInfo_Ver2;

import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.RiotAccount;
import lombok.Builder;
import java.util.List;

public record UserDTO<T extends UserDTO.BaseInterface>(Long memberId, RiotAccount riotAccount, T body) {

    // 중첩 Record 필드. 1. Request•Response, 2. Duo•Colosseum
    @Builder
    public record RequestDuo(
            UserInfo_Ver2 userInfo,
            DuoInfo_Ver2 duoInfo
    ) implements Request {
    }

    @Builder
    public record RequestColosseum(
            List<RiotAccount> partyMemberRiotAccountList
    ) implements Request {
    }

    @Builder
    public record ResponseDuo(
            UserInfo_Ver2 userInfo,
            DuoInfo_Ver2 duoInfo
    ) implements Response {
    }

    @Builder
    public record ResponseColosseum(
            List<PartyMemberInfo> partyMemberInfoList
    ) implements Response {
    }

    // 생성자 Builder
    @Builder
    public UserDTO {
    }

    // 인터페이스 태그
    public interface Request extends BaseInterface {
    }

    public interface Response extends BaseInterface {
    }

    public interface BaseInterface {
        default MatchingCategory getMatchingCategory() {
            if (this instanceof UserDTO.RequestDuo || this instanceof UserDTO.ResponseDuo)
                return MatchingCategory.Duo;
            else
                return MatchingCategory.Colosseum;
        }
    }
}
