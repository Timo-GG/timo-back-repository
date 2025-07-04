package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisScrimPageOnly;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RedisHash(value = "ScrimMyPage", timeToLive = 24 * 60 * 60)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisScrimPage {
    @Id
    private final UUID myPageUUID;

    Integer headCount;
    ScrimMapCode mapCode;

    /** Acceptor Field */
    private CertifiedMemberInfo acceptorCertifiedMemberInfo;
    private List<PartyMemberInfo> acceptorPartyInfo;
    private String acceptorMemo;

    /** Requestor Field */
    private CertifiedMemberInfo requestorCertifiedMemberInfo;
    private List<PartyMemberInfo> requestorPartyInfo;
    private String requestorMemo;

    private LocalDateTime updatedAt;

    /** 검색용 내부 인덱스 필드 */
    @Indexed private final MatchingCategory matchingCategory;
    @Indexed private final Long acceptorId;
    @Indexed private final Long requestorId;

    /** Redis 인스턴스 참조용 필드 */
    @Indexed private final UUID boardUUID;

    public static RedisScrimPage of(Integer headCount, ScrimMapCode mapCode, String acceptorMemo, String requestorMemo,
                                    CertifiedMemberInfo acceptorCertifiedMemberInfo, List<PartyMemberInfo> acceptorPartyInfo,
                                    CertifiedMemberInfo requestorCertifiedMemberInfo, List<PartyMemberInfo> requestorPartyInfo,
                                    Long acceptorId, Long requestorId, UUID boardUUID){
        LocalDateTime now = LocalDateTime.now();

        return new RedisScrimPage(UUID.randomUUID(), headCount, mapCode,
                               acceptorCertifiedMemberInfo, acceptorPartyInfo,acceptorMemo,
                               requestorCertifiedMemberInfo, requestorPartyInfo, requestorMemo,
                               now, MatchingCategory.SCRIM ,acceptorId, requestorId, boardUUID
        );
    }

    public static RedisScrimPage updateAcceptorVerificationFromProjection(
            RedisScrimPageOnly projection, String newVerificationType) {

        CertifiedMemberInfo updatedAcceptorInfo = CertifiedMemberInfo.withUpdatedVerificationType(
                projection.getAcceptorCertifiedMemberInfo(), newVerificationType);

        return RedisScrimPage.builder()
                .myPageUUID(projection.getMyPageUUID())
                .headCount(projection.getHeadCount())
                .mapCode(projection.getMapCode())
                .acceptorCertifiedMemberInfo(updatedAcceptorInfo)
                .acceptorPartyInfo(projection.getAcceptorPartyInfo())
                .acceptorMemo(projection.getAcceptorMemo())
                .requestorCertifiedMemberInfo(projection.getRequestorCertifiedMemberInfo())
                .requestorPartyInfo(projection.getRequestorPartyInfo())
                .requestorMemo(projection.getRequestorMemo())
                .updatedAt(projection.getUpdatedAt())
                .matchingCategory(projection.getMatchingCategory())
                .acceptorId(projection.getAcceptorId())
                .requestorId(projection.getRequestorId())
                .boardUUID(projection.getBoardUUID())
                .build();
    }

    /**
     * Projection에서 Requestor 인증 타입 업데이트하여 Entity 생성
     */
    public static RedisScrimPage updateRequestorVerificationFromProjection(
            RedisScrimPageOnly projection, String newVerificationType) {

        CertifiedMemberInfo updatedRequestorInfo = CertifiedMemberInfo.withUpdatedVerificationType(
                projection.getRequestorCertifiedMemberInfo(), newVerificationType);

        return RedisScrimPage.builder()
                .myPageUUID(projection.getMyPageUUID())
                .headCount(projection.getHeadCount())
                .mapCode(projection.getMapCode())
                .acceptorCertifiedMemberInfo(projection.getAcceptorCertifiedMemberInfo())
                .acceptorPartyInfo(projection.getAcceptorPartyInfo())
                .acceptorMemo(projection.getAcceptorMemo())
                .requestorCertifiedMemberInfo(updatedRequestorInfo)
                .requestorPartyInfo(projection.getRequestorPartyInfo())
                .requestorMemo(projection.getRequestorMemo())
                .updatedAt(projection.getUpdatedAt())
                .matchingCategory(projection.getMatchingCategory())
                .acceptorId(projection.getAcceptorId())
                .requestorId(projection.getRequestorId())
                .boardUUID(projection.getBoardUUID())
                .build();
    }


    private static CertifiedMemberInfo updateCertifiedMemberInfo(CertifiedMemberInfo original, String newVerificationType) {
        return CertifiedMemberInfo.withUpdatedVerificationType(original, newVerificationType);
    }
}

