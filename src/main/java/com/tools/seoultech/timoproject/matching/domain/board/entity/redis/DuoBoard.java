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
import java.util.List;
import java.util.UUID;

@RedisHash(value = "DuoBoard", timeToLive = 15 * 60)
@Getter @Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DuoBoard {
    @Id private final UUID boardUUID;

    @Indexed DuoMapCode mapCode;
    private String memo;

    /** CertifiedMemberInfo */
    // RiotAccount
    private String puuid;
    private String gameName;
    private String tagLine;
    private String profileUrl;

    // RankInfoDto 일부
    @Indexed private String tier;   // GOLD, PLATINUM, MASTER 등
    private String rank;

    // CompactMemberInfo 나머지 필드
    private List<String> most3Champ;
    private PlayPosition myPosition;

    // CertifiedMemberInfo 필드
    @Indexed private String univName;
    private String department;
    private Gender gender;
    private String mbti;

    /** UserInfo */
    @Indexed private VoiceChat myVoice;
    private PlayStyle myStyle;
    private PlayCondition myStatus;

    /** DuoInfo */
    private PlayPosition opponentPosition;
    private PlayStyle opponentStyle;


    /** 검색용 내부 인덱스 필드 */
    @Indexed private final Long memberId;
    @Indexed private final MatchingCategory matchingCategory;

    private LocalDateTime updatedAt;

    public static DuoBoard of(DuoMapCode mapCode, String memo, CertifiedMemberInfo memberInfo, UserInfo userInfo, DuoInfo duoInfo, Long memberId
    ){
        LocalDateTime now = LocalDateTime.now();
        return new DuoBoard(UUID.randomUUID(), mapCode, memo,
                // CertifiedMemberInfo
                memberInfo.getPuuid(), memberInfo.getGameName(), memberInfo.getTagLine(), memberInfo.getProfileUrl(), memberInfo.getTier(), memberInfo.getRank(), memberInfo.getMost3Champ(), memberInfo.getMyPosition(),
                memberInfo.getUnivName(), memberInfo.getDepartment(), memberInfo.getGender(), memberInfo.getMbti(),
                // UserInfo
                userInfo.getMyVoice(), userInfo.getMyStyle(), userInfo.getMyStatus(),
                // duoInfo
                duoInfo.getOpponentPosition(), duoInfo.getOpponentStyle(),
                // 나머지 필드
                memberId, MatchingCategory.DUO, now
        );
    }
}
