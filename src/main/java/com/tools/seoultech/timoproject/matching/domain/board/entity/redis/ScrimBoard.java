package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;


import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;
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

@RedisHash(value = "ScrimBoard", timeToLive = 15 * 60)
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScrimBoard {
    @Id
    private final UUID boardUUID;

    @Indexed private ScrimMapCode mapCode;
    private String memo;
    private Integer headCount;
    private CertifiedMemberInfo memberInfo;
    private List<PartyMemberInfo> partyInfo;

    /** 검색용 내부 인덱스 필드 */
    @Indexed private final Long memberId;
    @Indexed private final MatchingCategory matchingCategory;
    @Indexed private String tier;

    public static ScrimBoard of(ScrimMapCode mapCode, String memo, Integer headCount, CertifiedMemberInfo memberInfo, List<PartyMemberInfo> partyInfo, Long memberId
    ){
        return new ScrimBoard(UUID.randomUUID(), mapCode, memo, headCount,
                memberInfo, partyInfo, memberId,
                MatchingCategory.SCRIM, memberInfo.getRankInfo().getTier());
    }
}
