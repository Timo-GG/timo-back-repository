package com.tools.seoultech.timoproject.riot.facade;

import com.tools.seoultech.timoproject.global.annotation.PerformanceTimer;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.RiotAPIException;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.riot.dto.*;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiotFacadeImpl implements RiotFacade {

    @Qualifier("riotApiExecutor")
    private final TaskExecutor riotApiExecutor;

    private final RiotAPIService riotAPIService;

    @Override
    public RiotRankingDto getRiotRanking(String puuid) {
        List<String> most3ChampionNames = riotAPIService.getMost3ChampionNames(puuid);
        RankInfoDto soloRankInfoByPuuid = riotAPIService.getSoloRankInfoByPuuid(puuid);
        WinLossSummaryDto recentWinLossSummary = riotAPIService.getRecentWinLossSummary(puuid);
        String profileIconUrl = riotAPIService.getProfileIconUrlByPuuid(puuid);
        return RiotRankingDto.of(most3ChampionNames, profileIconUrl, soloRankInfoByPuuid, recentWinLossSummary);
    }

    @Override
    @PerformanceTimer
    public RecentMatchFullResponse getRecentMatchFullResponse(AccountDto.Request request) {
        // Step 1: 동기 호출로 기본 정보 준비
        AccountDto.Response account = riotAPIService.findUserAccount(request);
        String puuid = account.getPuuid();
        List<String> matchIds = riotAPIService.requestMatchList(puuid);

        // Step 2: 병렬 실행할 작업들 생성
        CompletableFuture<RankInfoDto> rankInfoFuture = createRankInfoFuture(puuid);
        CompletableFuture<String> profileIconFuture = createProfileIconFuture(puuid);
        List<CompletableFuture<MatchSummaryDTO>> matchSummariesFutures = createMatchSummaryFutures(puuid, matchIds);

        // Step 3: 모든 작업이 끝날 때까지 대기
        waitForAllFuturesToComplete(rankInfoFuture, profileIconFuture, matchSummariesFutures);

        // Step 4: 완료된 결과들을 조합하여 최종 응답 생성
        return combineResults(account, rankInfoFuture, profileIconFuture, matchSummariesFutures);
    }

    private CompletableFuture<RankInfoDto> createRankInfoFuture(String puuid) {
        return CompletableFuture.supplyAsync(() -> riotAPIService.getSoloRankInfoByPuuid(puuid), riotApiExecutor);
    }

    private CompletableFuture<String> createProfileIconFuture(String puuid) {
        return CompletableFuture.supplyAsync(() -> riotAPIService.getProfileIconUrlByPuuid(puuid), riotApiExecutor);
    }

    private List<CompletableFuture<MatchSummaryDTO>> createMatchSummaryFutures(String puuid, List<String> matchIds) {
        String runeData = riotAPIService.requestRuneData();
        return matchIds.stream()
                .map(matchId -> CompletableFuture.supplyAsync(() -> {
                    try {
                        DetailMatchInfoDTO detail = riotAPIService.requestMatchInfo(matchId, puuid, runeData);
                        return MatchSummaryDTO.from(detail);
                    } catch (Exception e) {
                        // 예외가 발생하면 로그를 남기고, 이 작업의 결과는 null로 반환
                        log.error("매치({}) 정보 처리 중 오류 발생. 이 매치는 결과에서 제외됩니다.", matchId, e.getMessage());
                        return null;
                    }
                }, riotApiExecutor))
                .toList();
    }

    private void waitForAllFuturesToComplete(CompletableFuture<?> rankInfoFuture,
                                             CompletableFuture<?> profileIconFuture,
                                             List<CompletableFuture<MatchSummaryDTO>> matchSummariesFutures) {
        List<CompletableFuture<?>> allFutures = new ArrayList<>();
        allFutures.add(rankInfoFuture);
        allFutures.add(profileIconFuture);
        allFutures.addAll(matchSummariesFutures);

        CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();
    }

    private RecentMatchFullResponse combineResults(AccountDto.Response account,
                                                   CompletableFuture<RankInfoDto> rankInfoFuture,
                                                   CompletableFuture<String> profileIconFuture,
                                                   List<CompletableFuture<MatchSummaryDTO>> matchSummariesFutures) {
        try {
            List<MatchSummaryDTO> recentMatch = matchSummariesFutures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList();

            return new RecentMatchFullResponse(
                    account.getGameName(),
                    account.getTagLine(),
                    profileIconFuture.get(),
                    rankInfoFuture.get(),
                    recentMatch
            );
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RiotAPIException("전적조회 결과 조합 과정에서 오류가 발생하였습니다.", ErrorCode.API_ACCESS_ERROR);
        }
    }
}
