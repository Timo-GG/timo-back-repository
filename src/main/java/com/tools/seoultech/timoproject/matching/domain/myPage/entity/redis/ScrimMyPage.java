package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
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
    private List<RiotAccount> acceptorPartyInfo;

    /** Requestor Field */
    private List<RiotAccount> requestorPartyInfo;

    /** 검색용 내부 인덱스 필드 */
    @Indexed private final MatchingCategory matchingCategory;
    @Indexed private final Long acceptorId;
    @Indexed private final Long requestorId;

    /** Redis 인스턴스 참조용 필드 */
    private final UUID boardUUID;

    public static ScrimMyPage of( List<RiotAccount> acceptorPartyInfo, List<RiotAccount> requestorPartyInfo,
                                  Long acceptorId, Long requestorId, UUID boardUUID){

        return new ScrimMyPage(UUID.randomUUID(),
                               acceptorPartyInfo, requestorPartyInfo,
                               MatchingCategory.SCRIM ,acceptorId, requestorId, boardUUID
        );
    }
}

