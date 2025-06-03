package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;


import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.Gender;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RedisHash(value = "ScrimBoard", timeToLive = 15 * 60)
@Getter @Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScrimBoard {
    @Id
    private final UUID boardUUID;

    @Indexed private ScrimMapCode mapCode;
    private String memo;
    private Integer headCount;

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

    // CertifiedMemberInfo 나머지 필드
    @Indexed private String univName;
    private String department;
    private Gender gender;
    private String mbti;

    /** PartyMemberInfo */
    private List<CompactMemberInfo> partyInfo;


    /** 검색용 내부 인덱스 필드 */
    @Indexed private final Long memberId;
    @Indexed private final MatchingCategory matchingCategory;

    private LocalDateTime updatedAt;

    public static ScrimBoard of(ScrimMapCode mapCode, String memo, Integer headCount, CertifiedMemberInfo memberInfo, List<CompactMemberInfo> partyInfo, Long memberId){
        LocalDateTime now = LocalDateTime.now();
        return new ScrimBoard(UUID.randomUUID(), mapCode, memo, headCount,
                // CompactMemberInfo
                memberInfo.getPuuid(), memberInfo.getGameName(), memberInfo.getTagLine(), memberInfo.getProfileUrl(), memberInfo.getTier(), memberInfo.getRank(), memberInfo.getMost3Champ(), memberInfo.getMyPosition(),
                // CertifiedMemberInfo 나머지 필드
                memberInfo.getUnivName(), memberInfo.getDepartment(), memberInfo.getGender(), memberInfo.getMbti(),
                // 나머지 필드
                partyInfo, memberId, MatchingCategory.SCRIM, now);
    }
}
