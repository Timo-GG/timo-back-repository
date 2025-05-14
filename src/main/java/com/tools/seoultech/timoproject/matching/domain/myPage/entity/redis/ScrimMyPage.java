package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.RiotAccount;
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
@AllArgsConstructor
public class ScrimMyPage {
    @Id
    private final UUID myPageUUID;

    /** Acceptor Field */
    private List<RiotAccount> acceptor_PartyInfo;

    /** Requestor Feild */
    private List<RiotAccount> requestor_PartyInfo;

    /** 검색용 내부 인덱스 필드 */
    @Indexed private final MatchingCategory matchingCategory;
    @Indexed private final Long acceptorId;
    @Indexed private final Long requestorId;

    /** Redis 인스턴스 참조용 필드 */
    private final UUID boardUUID;
}
