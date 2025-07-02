package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisDuoPageOnly;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.RiotVerificationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.UUID;

@RedisHash(value = "DuoMyPage", timeToLive = 15 * 60)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisDuoPage {
    @Id private final UUID myPageUUID;

    DuoMapCode mapCode;

    /** Acceptor Field */
    private CertifiedMemberInfo acceptorCertifiedMemberInfo;
    private UserInfo acceptorUserInfo;
    private String acceptorMemo;

    /** Requestor Field */
    private CertifiedMemberInfo requestorCertifiedMemberInfo;
    private UserInfo requestorUserInfo;
    private String requestorMemo;

    private LocalDateTime updatedAt;

    /** 검색용 내부 인덱스 필드 */
    @Indexed private final MatchingCategory matchingCategory;
    @Indexed private final Long acceptorId;
    @Indexed private final Long requestorId;

    /** Redis 인스턴스 참조용 필드 */
    @Indexed private final UUID boardUUID;


    public static RedisDuoPage of(DuoMapCode mapCode, String acceptorMemo, String requestorMemo,
                                  UserInfo acceptorUserInfo, CertifiedMemberInfo acceptorMemberInfo,
                                  UserInfo requestorUserInfo, CertifiedMemberInfo requestorMemberInfo,
                                  Long acceptorId, Long requestorId, UUID boardUUID) {
        LocalDateTime now = LocalDateTime.now();

        return new RedisDuoPage( UUID.randomUUID(), mapCode,
                                 acceptorMemberInfo, acceptorUserInfo, acceptorMemo,
                                 requestorMemberInfo, requestorUserInfo, requestorMemo,
                                 now, MatchingCategory.DUO, acceptorId, requestorId, boardUUID
        );
    }

    public static RedisDuoPage updateAcceptorVerificationFromProjection(
            RedisDuoPageOnly projection, String newVerificationType) {

        CertifiedMemberInfo updatedAcceptorInfo = CertifiedMemberInfo.withUpdatedVerificationType(
                projection.getAcceptorCertifiedMemberInfo(), newVerificationType);

        return RedisDuoPage.builder()
                .myPageUUID(projection.getMyPageUUID())
                .mapCode(projection.getMapCode())
                .acceptorCertifiedMemberInfo(updatedAcceptorInfo)
                .acceptorUserInfo(projection.getAcceptorUserInfo())
                .acceptorMemo(projection.getAcceptorMemo())
                .requestorCertifiedMemberInfo(projection.getRequestorCertifiedMemberInfo())
                .requestorUserInfo(projection.getRequestorUserInfo())
                .requestorMemo(projection.getRequestorMemo())
                .updatedAt(projection.getUpdatedAt())
                .matchingCategory(projection.getMatchingCategory())
                .acceptorId(projection.getAcceptorId())
                .requestorId(projection.getRequestorId())
                .boardUUID(projection.getBoardUUID())
                .build();
    }

    public static RedisDuoPage updateRequestorVerificationFromProjection(
            RedisDuoPageOnly projection, String newVerificationType) {

        CertifiedMemberInfo updatedRequestorInfo = CertifiedMemberInfo.withUpdatedVerificationType(
                projection.getRequestorCertifiedMemberInfo(), newVerificationType);

        return RedisDuoPage.builder()
                .myPageUUID(projection.getMyPageUUID())
                .mapCode(projection.getMapCode())
                .acceptorCertifiedMemberInfo(projection.getAcceptorCertifiedMemberInfo())
                .acceptorUserInfo(projection.getAcceptorUserInfo())
                .acceptorMemo(projection.getAcceptorMemo())
                .requestorCertifiedMemberInfo(updatedRequestorInfo)
                .requestorUserInfo(projection.getRequestorUserInfo())
                .requestorMemo(projection.getRequestorMemo())
                .updatedAt(projection.getUpdatedAt())
                .matchingCategory(projection.getMatchingCategory())
                .acceptorId(projection.getAcceptorId())
                .requestorId(projection.getRequestorId())
                .boardUUID(projection.getBoardUUID())
                .build();
    }

    /**
     * CertifiedMemberInfo의 verificationType 업데이트 헬퍼
     */
    private static CertifiedMemberInfo updateCertifiedMemberInfo(CertifiedMemberInfo original, String newVerificationType) {
        return CertifiedMemberInfo.withUpdatedVerificationType(original, newVerificationType);
    }
}