package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;


import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.*;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.UUID;

@RedisHash(value = "DuoBoard", timeToLive = 24 * 60 * 60)
@Getter @Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DuoBoard {
    @Id private final UUID boardUUID;

    @Indexed DuoMapCode mapCode;
    private String memo;
    private CertifiedMemberInfo memberInfo;

    /** UserInfo */
    @Indexed private PlayPosition myPosition;
    @Indexed private VoiceChat myVoice;
    private PlayStyle myStyle;
    private PlayCondition myStatus;

    /** DuoInfo */
    private PlayPosition opponentPosition;
    private PlayStyle opponentStyle;

    private LocalDateTime updatedAt;

    /** 검색용 내부 인덱스 필드 */
    @Indexed private final Long memberId;
    @Indexed private final MatchingCategory matchingCategory;
    @Indexed private String tier;


    public static DuoBoard of(DuoMapCode mapCode, String memo, CertifiedMemberInfo memberInfo, UserInfo userInfo, DuoInfo duoInfo, Long memberId
    ){
        LocalDateTime now = LocalDateTime.now();
        return new DuoBoard(UUID.randomUUID(), mapCode, memo, memberInfo,
                userInfo.getMyPosition(), userInfo.getMyVoice(), userInfo.getMyStyle(), userInfo.getMyStatus(),
                duoInfo.getOpponentPosition(), duoInfo.getOpponentStyle(),
                now, memberId, MatchingCategory.DUO, memberInfo.getRankInfo().getTier()
        );
    }
}
