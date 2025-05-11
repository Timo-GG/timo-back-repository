package com.tools.seoultech.timoproject.matching.domain.user.entity.redis;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.redis.om.spring.annotations.Searchable;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;

import java.util.List;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "matchingCategory",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RedisUser.Duo.class, name = "DUO"),
        @JsonSubTypes.Type(value = RedisUser.Colosseum.class, name = "COLOSSEUM")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class RedisUser {
    @Id
    private UUID uuid;

    @Indexed @Searchable
    private MemberAccount memberAccount;

    @Indexed
    private MatchingCategory matchingCategory;

    private CompactPlayerHistory compactPlayerHistory;

    @Document
    @Schema(description = "듀오 사용자")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Duo extends RedisUser {
        private UserInfo userInfo;
        private DuoInfo duoInfo;

        @Builder
        public Duo(UUID uuid, MatchingCategory matchingCategory, CompactPlayerHistory compactPlayerHistory,
                   MemberAccount memberAccount, UserInfo userInfo, DuoInfo duoInfo) {
            super(uuid, memberAccount, matchingCategory, compactPlayerHistory);
            this.userInfo = userInfo;
            this.duoInfo = duoInfo;
        }
    }


    @Document
    @Schema(description = "콜로세움 사용자")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Colosseum extends RedisUser {
        private List<PartyMemberInfo> partyMemberInfoList;

        @Builder
        public Colosseum(UUID uuid, MatchingCategory matchingCategory, CompactPlayerHistory compactPlayerHistory,
                         MemberAccount memberAccount, List<PartyMemberInfo> partyMemberInfoList) {
            super(uuid, memberAccount, matchingCategory, compactPlayerHistory);
            this.partyMemberInfoList = partyMemberInfoList;
        }
    }
}