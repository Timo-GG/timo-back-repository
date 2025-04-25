package com.tools.seoultech.timoproject.version2.matching.domain.user.dto;

import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.DuoInfo_Ver2;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.UserInfo_Ver2;

import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.RiotAccount;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RedisHash("UserDTO")
@RequiredArgsConstructor
@Getter
public class UserDTO<T extends UserDTO.UserDTOInterface> {
    public interface UserDTOInterface{}

    private final Long id;
    private final MatchingCategory matchingCategory;
    private final Long memberId;
    private final T dto;

    @Builder
    public record RequestDuoUser(
            UserInfo_Ver2 userInfo,
            DuoInfo_Ver2 duoInfo
    )implements UserDTOInterface{}

    @Builder
    public record RequestColosseumUser(
            List<RiotAccount> partyMemberRiotAccountList
    )implements UserDTOInterface{}

    @Builder
    public record ResponseDuoUser(
            UserInfo_Ver2 userInfo,
            DuoInfo_Ver2 duoInfo
    )implements UserDTOInterface{}

    @Builder
    public record ResponseColloseumUser(
                List<PartyMemberInfo> partyMemberInfoList
    )implements UserDTOInterface{}
}
