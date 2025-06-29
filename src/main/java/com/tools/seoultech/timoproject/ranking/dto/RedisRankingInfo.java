package com.tools.seoultech.timoproject.ranking.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.Gender;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@RedisHash(value = "rankingInfo")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisRankingInfo implements Serializable {
    @Id
    private String id;

    private Long memberId;
    private String puuid;
    
    // 소환사 정보
    private String gameName;
    private String tagLine;
    private String profileIconUrl;
    private String verificationType;

    // 대학 정보
    private String university;
    private String department;
    
    // 라이엇 API 정보
    private String tier;
    private String rank;
    private int lp;
    private List<String> mostChampions;
    private int wins;
    private int losses;
    
    // 랭킹 점수
    private Integer score;

    // RankingInfo
    private String mbti;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private PlayPosition position;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Gender gender;
    private String memo;


    // 랭킹 점수 계산
    public static int calculateScore(String tier, String rank, int lp) {
        int baseScore = switch (tier.toUpperCase()) {
            case "IRON" -> 0;
            case "BRONZE" -> 400;
            case "SILVER" -> 800;
            case "GOLD" -> 1200;
            case "PLATINUM" -> 1600;
            case "EMERALD" -> 2000;
            case "DIAMOND" -> 2400;
            case "MASTER" -> 2800;
            case "GRANDMASTER" -> 3200;
            case "CHALLENGER" -> 3600;
            default -> 0;
        };
        
        int rankScore = switch (rank) {
            case "IV" -> 0;
            case "III" -> 100;
            case "II" -> 200;
            case "I" -> 300;
            default -> 0;
        };
        
        return baseScore + rankScore + lp;
    }

    public void updateId(Long memberId) {
        this.id = memberId.toString();
    }

    public void updateRankingInfo(RankingUpdateRequestDto dto) {
        if (dto.mbti() != null) this.mbti = dto.mbti();
        if (dto.memo() != null) this.memo = dto.memo();
        if (dto.position() != null) this.position = dto.position();
        if (dto.gender() != null) this.gender = dto.gender();
        if (dto.department() != null) this.department = dto.department();
    }

    public static RedisRankingInfo from(Long memberId, Member account, RiotRankingDto riotRankingDto) {
        var riotAccount = account.getRiotAccount();
        var univInfo = account.getCertifiedUnivInfo();
        var soloRank = riotRankingDto.soloRankInfo();

        return RedisRankingInfo.builder()
                .memberId(memberId)
                .puuid(riotAccount.getPuuid())
                .gameName(riotAccount.getGameName())
                .verificationType(riotAccount.getVerificationType().name())
                .profileIconUrl(riotRankingDto.profileIconUrl())
                .tagLine(riotAccount.getTagLine())
                .university(univInfo.getUnivName())
                .department(univInfo.getDepartment())
                .tier(soloRank.getTier())
                .rank(soloRank.getRank())
                .lp(soloRank.getLp())
                .mostChampions(riotRankingDto.most3ChampionNames())
                .wins(riotRankingDto.recentWinLossSummary().wins())
                .losses(riotRankingDto.recentWinLossSummary().losses())
                .score(calculateScore(soloRank.getTier(), soloRank.getRank(), soloRank.getLp()))
                .build();
    }
}