package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;
import java.util.UUID;

@RedisHash(value = "ScrimMyPage", timeToLive = 15 * 60)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrimMyPage {
    @Id
    private final UUID myPageUUID;

    /** Acceptor Field */
    private CertifiedMemberInfo acceptorCertifiedMemberInfo;
    private List<PartyMemberInfo> acceptorPartyInfo;

    /** Requestor Field */
    private CertifiedMemberInfo requestorCertifiedMemberInfo;
    private List<PartyMemberInfo> requestorPartyInfo;

    /** 검색용 내부 인덱스 필드 */
    @Indexed private final MatchingCategory matchingCategory;
    @Indexed private final Long acceptorId;
    @Indexed private final Long requestorId;

    /** Redis 인스턴스 참조용 필드 */
    private final UUID boardUUID;

    public static ScrimMyPage of( CertifiedMemberInfo acceptorCertifiedMemberInfo, List<PartyMemberInfo> acceptorPartyInfo,
                                   CertifiedMemberInfo requestorCertifiedMemberInfo, List<PartyMemberInfo> requestorPartyInfo,
                                  Long acceptorId, Long requestorId, UUID boardUUID){

        return new ScrimMyPage(UUID.randomUUID(),
                               acceptorCertifiedMemberInfo, acceptorPartyInfo,
                               requestorCertifiedMemberInfo, requestorPartyInfo,
                               MatchingCategory.SCRIM ,acceptorId, requestorId, boardUUID
        );
    }
}

