package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.riot.dto.MatchSummaryDTO;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;
import java.util.UUID;

@RedisHash(value = "DuoMyPage", timeToLive = 15 * 60)
@Getter
@Builder
@AllArgsConstructor
public class DuoMyPage {
    @Id private final UUID myPageUUID;

    /** Acceptor Field */
    private UserInfo acceptorUserInfo;
    private DuoInfo acceptorDuoInfo;
    private CompactPlayerHistory acceptorCompactPlayerHistory;

    private List<String> most3Champ;
    private List<MatchSummaryDTO> last10Match;

    /** Requestor Feild */
    private UserInfo requestorUserInfo;
    private DuoInfo requestorDuoInfo;

    /** 검색용 내부 인덱스 필드 */
    @Indexed private final MatchingCategory matchingCategory;
    @Indexed private final Long acceptorId;
    @Indexed private final Long requestorId;

    /** Redis 인스턴스 참조용 필드 */
    private final UUID boardUUID;
}

interface getData {
    UUID getMyPageUUID();

}