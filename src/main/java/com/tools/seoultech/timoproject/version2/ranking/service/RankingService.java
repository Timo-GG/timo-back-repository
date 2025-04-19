package com.tools.seoultech.timoproject.version2.ranking.service;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.version2.memberAccount.MemberAccountRepository;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.version2.ranking.RankingInfo;
import com.tools.seoultech.timoproject.version2.ranking.dto.Redis_RankingInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingService {
    private final RankingInfoRepository rankingInfoRepository;
    private final RankingRedisService rankingRedisService;
    private final MemberAccountRepository memberAccountRepository;

    public void createRanking(Long memberId, RiotRankingDto riotRankingDto) {
        MemberAccount account = memberAccountRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (account.getRiotAccount() == null || account.getCertifiedUnivInfo() == null) {
            throw new BusinessException(ErrorCode.INVALID_RANKING_INFO);
        }

        Redis_RankingInfo redisRankingInfo = Redis_RankingInfo.builder()
                .memberId(memberId)
                .puuid(account.getRiotAccount().getPuuid())
                .gameName(account.getRiotAccount().getAccountName())
                .tagLine(account.getRiotAccount().getAccountTag())
                .university(account.getCertifiedUnivInfo().getUnivName())
                .department(account.getCertifiedUnivInfo().getDepartment())
                .tier(riotRankingDto.soloRankInfo().getTier())
                .rank(riotRankingDto.soloRankInfo().getRank())
                .lp(riotRankingDto.soloRankInfo().getLp())
                .mostChampions(riotRankingDto.most3ChampionNames())
                .wins(riotRankingDto.recentWinLossSummary().wins())
                .losses(riotRankingDto.recentWinLossSummary().losses())
                .score(Redis_RankingInfo.calculateScore(
                        riotRankingDto.soloRankInfo().getTier(),
                        riotRankingDto.soloRankInfo().getRank(),
                        riotRankingDto.soloRankInfo().getLp()))
                .build();

        rankingRedisService.saveRankInfo(memberId, redisRankingInfo);
        log.info("[랭킹 Redis 등록] memberId={} 등록 완료", memberId);
    }


    /**
     * RankingInfo 업데이트 - 회원가입 완료 단계
     */
    @Transactional
    public void updateRankingInfo(Long memberId, RankingInfo rankingInfo) {
        try {
            // 1. DB에 RankingInfo 저장
            rankingInfoRepository.save(rankingInfo);

            log.info("RankingInfo 업데이트 완료: memberId={}", memberId);
        } catch (Exception e) {
            log.error("RankingInfo 업데이트 중 오류 발생", e);
            throw new BusinessException(ErrorCode.API_ACCESS_ERROR);
        }
    }
}