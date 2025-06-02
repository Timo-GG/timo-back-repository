package com.tools.seoultech.timoproject.ranking.service;


import com.tools.seoultech.timoproject.ranking.facade.RankingFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class RankingUpdateScheduler {

    private final RankingFacade rankingFacade;

    /**
     * 매 1시간마다 모든 회원의 랭킹 정보 업데이트
     */
    @Scheduled(fixedDelayString = "${ranking.update.delay:3600000}")
    public void updateAllRankings() {
        log.info("=== 랭킹 업데이트 스케줄러 시작 ===");

        try {
            rankingFacade.updateAllRankingsFromRiotAPI();
            log.info("=== 랭킹 업데이트 스케줄러 완료 ===");

        } catch (Exception e) {
            log.error("랭킹 업데이트 스케줄러 실행 중 오류 발생", e);
        }
    }

    /**
     * 특정 회원의 랭킹 정보 즉시 업데이트
     */
    public void updateMemberRankingNow(Long memberId) {
        try {
            rankingFacade.updateRankingFromRiotAPI(memberId);
            log.info("회원 랭킹 즉시 업데이트 완료: memberId={}", memberId);

        } catch (Exception e) {
            log.error("회원 랭킹 즉시 업데이트 실패: memberId={}", memberId, e);
            throw e;
        }
    }
}