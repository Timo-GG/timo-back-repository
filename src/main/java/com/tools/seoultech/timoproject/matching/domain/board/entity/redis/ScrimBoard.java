package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;


import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumMapCode;
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

@RedisHash(value = "ScrimBoard", timeToLive = 15 * 60)
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScrimBoard {
    @Id
    private final UUID boardUUID;

    @Indexed private ColosseumMapCode mapCode;
    private String memo;
    private Integer headCount;
    private List<RiotAccount> partyInfo;
    private CompactPlayerHistory compactPlayerHistory;

    /** 검색용 내부 인덱스 필드 */
    @Indexed private final Long memberId;
    @Indexed private final MatchingCategory matchingCategory;
    @Indexed private String tier;

    public static ScrimBoard of(ColosseumMapCode mapCode, String memo, Integer headCount, List<RiotAccount> partyInfo, CompactPlayerHistory compactPlayerHistory, Long memberId){
        return new ScrimBoard(UUID.randomUUID(), mapCode, memo, headCount,
                partyInfo, compactPlayerHistory, memberId,
                MatchingCategory.SCRIM, compactPlayerHistory.getRankInfo().getTier());
    }
}
