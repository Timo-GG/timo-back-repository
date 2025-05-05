package com.tools.seoultech.timoproject.matching.domain.user.entity.redis;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.DuoInfo_Ver2;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.UserInfo_Ver2;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.RiotAccount;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;
import java.util.UUID;

@RedisHash(value = "redisUser", timeToLive = 15 * 60)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "matchingCategory",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RedisUser.Duo.class, name = "Duo"),
        @JsonSubTypes.Type(value = RedisUser.Colosseum.class, name = "Colosseum")
})
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RedisUser {
    @Id
    private final UUID uuid;

    private final Long memberId;

//    Note: 중첩구조가 복잡해서 Flat 필드로 사용하고 싶다면
//      @JsonUnwrapped(prefix = "riot")을 사용할것.
    private final RiotAccount riotAccount;

    @Indexed @NotNull
    // 서버 내부 필드. 역직렬화에 사용되지 않음. 직렬화에서는 편의성 때문에 보여주긴 함.
    private final MatchingCategory matchingCategory;

    @Getter
    public static class Duo extends RedisUser{
        private UserInfo_Ver2 userInfo;
        private DuoInfo_Ver2 duoInfo;

        @Builder
        @PersistenceCreator
        public Duo(RiotAccount riotAccount, Long memberId, UserInfo_Ver2 userInfo, DuoInfo_Ver2 duoInfo) {
            super(UUID.randomUUID(), memberId, riotAccount, MatchingCategory.Duo);
            this.userInfo = userInfo;
            this.duoInfo = duoInfo;
        }
    }

    @Getter
    public static class Colosseum extends RedisUser{
        private List<PartyMemberInfo> partyMemberInfoList;

        @Builder
        @PersistenceCreator
        public Colosseum(RiotAccount riotAccount, Long memberId, List<PartyMemberInfo> partyMemberInfoList) {
            super(UUID.randomUUID(), memberId, riotAccount, MatchingCategory.Colosseum);
            this.partyMemberInfoList = partyMemberInfoList;
        }
    }
}
