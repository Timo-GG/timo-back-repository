package com.tools.seoultech.timoproject.version2.matching.domain.user.dto;

import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.DuoInfo_Ver2;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.UserInfo_Ver2;

import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.RiotAccount;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserDTO<T extends UserDTO.BaseInterface> {

    // 공통 필드.
    private final Long memberId;

    private final RiotAccount riotAccount;

    private final T body;

    // 중첩 Record 필드. 1. Request•Response, 2. Duo•Colosseum
    @Builder
    public record RequestDuo(
            UserInfo_Ver2 userInfo,
            DuoInfo_Ver2 duoInfo
    )implements Request {}

    @Builder
    public record RequestColosseum(
            List<RiotAccount> partyMemberRiotAccountList
    )implements Request {}

    @Builder
    public record ResponseDuo(
            UserInfo_Ver2 userInfo,
            DuoInfo_Ver2 duoInfo
    )implements Response {}

    @Builder
    public record ResponseColosseum(
                List<PartyMemberInfo> partyMemberInfoList
    )implements Response {}

    // 생성자 Builder
    @Builder
    public UserDTO(Long memberId, RiotAccount riotAccount, T body){
        this.memberId = memberId;
        this.riotAccount = riotAccount;
        this.body = body;
    }

    // 인터페이스 태그
    public interface Request extends BaseInterface {}
    public interface Response extends BaseInterface {}
    public interface BaseInterface {
        default MatchingCategory getMatchingCategory(){
            if(this instanceof UserDTO.RequestDuo || this instanceof UserDTO.ResponseDuo)
                return MatchingCategory.Duo;
            else
                return MatchingCategory.Colosseum;
        }
    }
}
