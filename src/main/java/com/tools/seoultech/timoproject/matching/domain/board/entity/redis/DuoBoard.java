package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.*;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.riot.dto.MatchSummaryDTO;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.expression.spel.ast.Projection;

import java.util.List;
import java.util.UUID;

@RedisHash(value = "DuoBoard", timeToLive = 15 * 60)
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DuoBoard {
    @Id private final UUID boardUUID;

    @Indexed DuoMapCode mapCode;
    private String memo;
    private CompactPlayerHistory compactPlayerHistory;

    /** UserInfo */
    @Indexed private PlayPosition myPosition;
    @Indexed private VoiceChat myVoice;
    private PlayCondition myStatus;
    private PlayStyle myStyle;

    /** DuoInfo */
    private PlayPosition opponentPosition;
    private PlayStyle opponentStyle;

    /** 검색용 내부 인덱스 필드 */
    @Indexed private final Long memberId;
    @Indexed private final MatchingCategory matchingCategory;
    @Indexed private String tier;

    public static DuoBoard of(DuoMapCode mapCode, String memo, CompactPlayerHistory compactPlayerHistory, UserInfo userInfo, DuoInfo duoInfo, Long memberId
    ){
        return new DuoBoard(UUID.randomUUID(), mapCode, memo, compactPlayerHistory,
                userInfo.getMyPosition(), userInfo.getMyVoice(), userInfo.getMyStatus(), userInfo.getMyStyle(),
                duoInfo.getOpponentPosition(), duoInfo.getOpponentStyle(),
                memberId, MatchingCategory.DUO, compactPlayerHistory.getRankInfo().getTier()
        );
    }
}
