package com.tools.seoultech.timoproject.version2.matching.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.DuoInfo_Ver2;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.UserInfo_Ver2;

import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.RiotAccount;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;
import java.util.UUID;

@Getter
public class UserDTO<T extends UserDTO.UserDTOInterface> {

    // 공통 필드.
    private final Long memberId;

    private final RiotAccount riotAccount;

    private final T body;

    // 중첩 Record 필드. 1. Request•Response, 2. Duo•Colosseum
    @Builder
    public record RequestDuoUser(
            UserInfo_Ver2 userInfo,
            DuoInfo_Ver2 duoInfo
    )implements UserRequestDTOInterface{}

    @Builder
    public record RequestColosseumUser(
            List<RiotAccount> partyMemberRiotAccountList
    )implements UserRequestDTOInterface{}

    @Builder
    public record ResponseDuoUser(
            UserInfo_Ver2 userInfo,
            DuoInfo_Ver2 duoInfo
    )implements UserResponseDTOInterface{}

    @Builder
    public record ResponseColoseumUser(
                List<PartyMemberInfo> partyMemberInfoList
    )implements UserResponseDTOInterface{}

    // 생성자 Builder
    @Builder
    public UserDTO(Long memberId, RiotAccount riotAccount, T body){
        this.memberId = memberId;
        this.riotAccount = riotAccount;
        this.body = body;
    }

    // 인터페이스 태그
    public interface UserRequestDTOInterface extends UserDTOInterface{}
    public interface UserResponseDTOInterface extends UserDTOInterface{}
    public interface UserDTOInterface{
        default MatchingCategory getMatchingCategory(){
            if(this instanceof RequestDuoUser || this instanceof ResponseDuoUser)
                return MatchingCategory.Duo;
            else
                return MatchingCategory.Colosseum;
        }
    }
}
