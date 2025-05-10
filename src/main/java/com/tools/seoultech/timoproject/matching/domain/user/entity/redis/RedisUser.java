package com.tools.seoultech.timoproject.matching.domain.user.entity.redis;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.enumType.Gender;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.RiotAccount;
import io.swagger.v3.oas.annotations.media.Schema;
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
public abstract class RedisUser {
    @Id
    private final UUID uuid;

    @Indexed
    private final Long memberId;

    private final CertifiedUnivInfo certifiedUnivInfo;
    private final Gender gender;            // 성별
    private final String mbti;              // MBTI
    private final RiotAccount riotAccount;  // 라이엇 계정
    private final CompactPlayerHistory compactPlayerHistory;

    @Indexed @NotNull
    // 서버 내부 필드. 역직렬화에 사용되지 않음. 직렬화에서는 편의성 때문에 보여주긴 함.
    private final MatchingCategory matchingCategory;

    /** Redis 조회용 생성자 */
    @PersistenceCreator
    protected RedisUser(UUID uuid,
                        Long memberId, CertifiedUnivInfo certifiedUnivInfo, Gender gender, String mbti,
                        RiotAccount riotAccount, CompactPlayerHistory compactPlayerHistory,
                        MatchingCategory matchingCategory) {
        this.uuid = uuid;
        this.memberId = memberId;
        this.certifiedUnivInfo = certifiedUnivInfo;
        this.gender = gender;
        this.mbti = mbti;
        this.riotAccount = riotAccount;
        this.compactPlayerHistory = compactPlayerHistory;
        this.matchingCategory = matchingCategory;
    }

    /** Builder용 생성자 (서브클래스에서 호출) */
    protected RedisUser(Long memberId,
                        CertifiedUnivInfo certifiedUnivInfo,
                        Gender gender, String mbti,
                        RiotAccount riotAccount,
                        CompactPlayerHistory compactPlayerHistory,
                        MatchingCategory matchingCategory) {
        this(UUID.randomUUID(), memberId, certifiedUnivInfo, gender, mbti, riotAccount, compactPlayerHistory, matchingCategory);
    }

    @Getter
    @Schema(description = "듀오 사용자")
    public static class Duo extends RedisUser {
        private UserInfo userInfo;
        private DuoInfo duoInfo;

        /** Redis 조회용 생성자 */
        @PersistenceCreator
        protected Duo(UUID uuid,
                      Long memberId,
                      CertifiedUnivInfo certifiedUnivInfo,
                      Gender gender,
                      String mbti,
                      RiotAccount riotAccount,
                      CompactPlayerHistory compactPlayerHistory,
                      MatchingCategory matchingCategory,
                      UserInfo userInfo,
                      DuoInfo duoInfo) {
            super(uuid, memberId, certifiedUnivInfo, gender, mbti, riotAccount, compactPlayerHistory, matchingCategory);
            this.userInfo = userInfo;
            this.duoInfo  = duoInfo;
        }

        /** Builder용 생성자: 신규 사용자 생성 시 호출 */
        @Builder
        public Duo(RiotAccount riotAccount,
                   CompactPlayerHistory compactPlayerHistory,
                   Long memberId,
                   CertifiedUnivInfo certifiedUnivInfo,
                   Gender gender,
                   String mbti,
                   UserInfo userInfo,
                   DuoInfo duoInfo) {
            super(memberId, certifiedUnivInfo, gender, mbti, riotAccount, compactPlayerHistory, MatchingCategory.Duo);
            this.userInfo = userInfo;
            this.duoInfo  = duoInfo;
        }
    }

    @Getter
    @Schema(description = "콜로세움 사용자")
    public static class Colosseum extends RedisUser {
        private List<PartyMemberInfo> partyMemberInfoList;

        /**
         * Redis 조회용 생성자
         */
        @PersistenceCreator
        protected Colosseum(UUID uuid,
                            Long memberId,
                            CertifiedUnivInfo certifiedUnivInfo,
                            Gender gender,
                            String mbti,
                            RiotAccount riotAccount,
                            CompactPlayerHistory compactPlayerHistory,
                            MatchingCategory matchingCategory,
                            List<PartyMemberInfo> partyMemberInfoList) {

            super(uuid, memberId, certifiedUnivInfo, gender, mbti, riotAccount, compactPlayerHistory, matchingCategory);
            this.partyMemberInfoList = partyMemberInfoList;
        }

        /**
         * Builder용 생성자
         */
        @Builder
        public Colosseum(Long memberId,
                         CertifiedUnivInfo certifiedUnivInfo,
                         Gender gender,
                         String mbti,
                         RiotAccount riotAccount,
                         CompactPlayerHistory compactPlayerHistory,
                         List<PartyMemberInfo> partyMemberInfoList) {

            super(memberId, certifiedUnivInfo, gender, mbti, riotAccount, compactPlayerHistory, MatchingCategory.Colosseum);
            this.partyMemberInfoList = partyMemberInfoList;
        }
    }

}