package com.tools.seoultech.timoproject.riot.service;

import com.tools.seoultech.timoproject.global.annotation.PerformanceTimer;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.riot.DataDragonClient;
import com.tools.seoultech.timoproject.riot.RiotAsiaApiClient;
import com.tools.seoultech.timoproject.riot.RiotKrApiClient;
import com.tools.seoultech.timoproject.riot.dto.*;
import com.tools.seoultech.timoproject.global.exception.RiotAPIException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClientResponseException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class RiotAPIService {
    private static final String DDRAGON_URL = "https://ddragon.leagueoflegends.com";
    private static final String QUEUE_TYPE_SOLO = "RANKED_SOLO_5x5";
    private static final String DEFAULT_DDRAGON_VERSION = "14.23.1"; // Fallback 버전
    private static final String UNKNOWN_CHAMPION_NAME = "Unknown";
    private static final int MATCH_LIST_START_INDEX = 0;
    private static final int RECENT_MATCH_COUNT = 10;
    private static final int TOP_CHAMPION_MASTERY_COUNT = 3;
    private static final long MATCH_SUMMARIES_TIMEOUT_SECONDS = 5L; // 매치 요약 타임아웃 5초
    private static final long WIN_LOSS_SUMMARY_TIMEOUT_SECONDS = 5L;  // 승패 요약 타임아웃 5초

    private final RiotAsiaApiClient asiaApiClient;
    private final RiotKrApiClient krApiClient;
    private final DataDragonClient dataDragonClient;
    private final ChampionCacheService championCacheService;
    @Autowired
    @Qualifier("riotApiExecutor")
    private TaskExecutor riotApiExecutor;
    @Value("${api_key}")
    private String api_key;
    private String ddragonVersion;

    @PostConstruct
    private void fetchLatestDdragonVersion() {
        try {
            List<String> versions = dataDragonClient.getVersions();
            if (!versions.isEmpty()) {
                ddragonVersion = versions.get(0);
                log.info("DataDragon 최신 버전: {}", ddragonVersion);
                return;
            }
            log.warn("DataDragon 버전 리스트가 비어있음");
        } catch (Exception e) {
            log.error("DataDragon 버전 조회 실패", e);
        }
        ddragonVersion = DEFAULT_DDRAGON_VERSION;
        log.info("DataDragon 버전 fallback: {}", ddragonVersion);
    }

    private String encodeUrlParameter(String param) {
        try {
            return java.net.URLEncoder.encode(param, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20");
        } catch (Exception e) {
            log.error("URL 인코딩 실패: {}", param, e);
            throw new RuntimeException("URL 인코딩 실패: " + param, e);
        }
    }

    @Transactional
    @PerformanceTimer
    @Cacheable(value = "accounts", key = "#dto.gameName + '-' + #dto.tagLine", unless = "#result == null")
    public AccountDto.Response findUserAccount(@Valid AccountDto.Request dto) {
        try {
            String gameName = dto.getGameName();
            String tagLine = dto.getTagLine();

            AccountDto.Response response = asiaApiClient.getAccountByRiotId(gameName, tagLine, api_key);
            log.info("계정 정보 조회 성공: {}", dto.getGameName());
            return response;
        } catch (Exception e) {
            log.error("라이엇 API 호출 중 예외 발생", e);
            throw handleApiError(e);
        }
    }

    @Cacheable(value = "matches", key = "#matchid")
    public MatchInfoDTO requestMatchInfoRaw(String matchid) {
        try {
            String encodedMatchId = encodeUrlParameter(matchid);
            String response = asiaApiClient.getMatchInfo(encodedMatchId, api_key);
            return MatchInfoDTO.of(response);
        } catch (Exception e) {
            log.error("매치 정보 조회 중 예외 발생", e);
            throw handleApiError(e);
        }
    }

    public DetailMatchInfoDTO requestMatchInfo(String matchid, String my_puuid, String requestRuneDataString) {
        try {
            MatchInfoDTO matchInfoDTO = requestMatchInfoRaw(matchid);
            return DetailMatchInfoDTO.of(matchInfoDTO, my_puuid, requestRuneDataString);
        } catch (Exception e) {
            log.error("상세 매치 정보 변환 중 예외 발생", e);
            throw new RiotAPIException("상세 매치 정보 변환 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    @Cacheable("runeData")
    public String requestRuneData() {
        try {
            String response = dataDragonClient.getRuneData(ddragonVersion);
            log.info("룬 데이터 API 응답 받음");
            return response;
        } catch (Exception e) {
            log.error("룬 데이터 조회 중 예외 발생", e);
            throw new RiotAPIException("룬 데이터 조회 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    @Cacheable(value = "matchLists", key = "#puuid")
    public List<String> requestMatchList(String puuid) {
        try {
            String encodedPuuid = encodeUrlParameter(puuid);
            List<String> matchIds = asiaApiClient.getMatchList(encodedPuuid, MATCH_LIST_START_INDEX, RECENT_MATCH_COUNT, api_key);
            log.info("매치 목록 조회 성공: {} 개", matchIds.size());
            return matchIds;
        } catch (Exception e) {
            log.error("매치 목록 조회 중 예외 발생", e);
            throw handleApiError(e);
        }
    }

    public List<String> getMost3ChampionNames(String puuid) {
        try {
            List<Map<String, Object>> masteryList = krApiClient.getChampionMasteries(puuid, api_key);
            Map<Integer, String> champMap = championCacheService.getChampionIdToNameMap();

            log.info("챔피언 숙련도 API 응답 받음: {}", puuid);
            return masteryList.stream()
                    .sorted((a, b) -> Integer.compare((int) b.get("championPoints"), (int) a.get("championPoints")))
                    .limit(TOP_CHAMPION_MASTERY_COUNT)
                    .map(champ -> {
                        Integer champId = (Integer) champ.get("championId");
                        return champMap.getOrDefault(champId, UNKNOWN_CHAMPION_NAME);
                    })
                    .toList();
        } catch (Exception e) {
            log.warn("챔피언 숙련도 조회 실패, 빈 리스트 반환", e);
            return Collections.emptyList();
        }
    }

    @Transactional
    @PerformanceTimer
    public List<MatchSummaryDTO> getRecentMatchSummaries(String puuid) {
        try {
            List<String> matchIds = requestMatchList(puuid);
            if (matchIds.isEmpty()) {
                log.info("최근 매치 정보가 없습니다: {}", puuid);
                return Collections.emptyList();
            }
            String runeData = requestRuneData();
            List<CompletableFuture<MatchSummaryDTO>> futures = matchIds.stream()
                    .map(matchId -> CompletableFuture.supplyAsync(() -> {
                        try {
                            DetailMatchInfoDTO detail = requestMatchInfo(matchId, puuid, runeData);
                            return MatchSummaryDTO.from(detail);
                        } catch (Exception e) {
                            log.error("매치 요약 정보 생성 중 오류 발생: {}", matchId, e);
                            return null;
                        }
                    }, riotApiExecutor))
                    .toList();

            CompletableFuture<Void> allOf = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            );
            List<MatchSummaryDTO> summaries = allOf.thenApply(v ->
                            futures.stream()
                                    .map(CompletableFuture::join)
                                    .filter(Objects::nonNull)
                                    .toList()
            ).get(MATCH_SUMMARIES_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            log.info("매치 요약 정보 생성 완료: {} 개", summaries.size());
            return summaries;

        } catch (TimeoutException e) {
            log.error("매치 요약 정보 생성 타임아웃", e);
            throw new RiotAPIException("매치 정보 조회 시간 초과", ErrorCode.API_ACCESS_ERROR);
        } catch (Exception e) {
            log.error("최근 매치 요약 정보 생성 중 예외 발생", e);
            throw new RiotAPIException("최근 매치 요약 정보 생성 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    public WinLossSummaryDto getRecentWinLossSummary(String puuid) {
        try {
            List<String> matchIds = requestMatchList(puuid);
            if (matchIds.isEmpty()) {
                log.info("최근 매치가 없습니다: {}", puuid);
                return WinLossSummaryDto.builder().wins(0).losses(0).build();
            }

            List<CompletableFuture<Boolean>> futures = matchIds.stream()
                    .map(matchId -> CompletableFuture.supplyAsync(() -> {
                        try {
                            MatchInfoDTO matchInfoDTO = requestMatchInfoRaw(matchId);
                            return matchInfoDTO.getMyInfo(puuid).getWin();
                        } catch (Exception e) {
                            log.warn("매치 승패 확인 실패: {}", matchId, e);
                            return null;
                        }
                    }, riotApiExecutor))
                    .toList();

            List<Boolean> results = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            ).thenApply(v ->
                            futures.stream()
                                    .map(CompletableFuture::join)
                                    .filter(Objects::nonNull)
                                    .toList()
                    // ✅ 타임아웃 값을 상수로 대체 및 조정
            ).get(WIN_LOSS_SUMMARY_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            Map<Boolean, Long> winLossCount = results.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            int wins = winLossCount.getOrDefault(true, 0L).intValue();
            int losses = winLossCount.getOrDefault(false, 0L).intValue();

            log.info("승패 요약 조회 완료 - 승: {}, 패: {}", wins, losses);
            return WinLossSummaryDto.builder()
                    .wins(wins)
                    .losses(losses)
                    .build();

        } catch (TimeoutException e) {
            log.error("승패 요약 조회 타임아웃", e);
            throw new RiotAPIException("승패 정보 조회 시간 초과", ErrorCode.API_ACCESS_ERROR);
        } catch (Exception e) {
            log.error("최근 승/패 요약 조회 실패", e);
            throw new RiotAPIException("최근 승패 정보 조회 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    @PerformanceTimer
    @Cacheable(value = "rankInfo", key = "#puuid")
    public RankInfoDto getSoloRankInfoByPuuid(String puuid) {
        try {
            List<Map<String, Object>> rankList = krApiClient.getRankInfo(puuid, api_key);
            return findSoloRankInfo(rankList, QUEUE_TYPE_SOLO);
        } catch (Exception e) {
            log.error("랭크 정보 조회 중 예외 발생", e);
            throw handleApiError(e);
        }
    }

    @PerformanceTimer
    @Cacheable(value = "profileIcons", key = "#puuid")
    public String getProfileIconUrlByPuuid(String puuid) {
        try {
            Map<String, Object> data = krApiClient.getSummonerInfo(puuid, api_key);
            Integer iconId = (Integer) data.get("profileIconId");

            if (iconId == null) {
                log.warn("profileIconId 누락: {}", puuid);
                return null;
            }

            return DDRAGON_URL + "/cdn/" + ddragonVersion + "/img/profileicon/" + iconId + ".png";

        } catch (Exception e) {
            log.error("프로필 이미지 URL 생성 실패", e);
            throw new RiotAPIException("프로필 이미지 조회 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    private RankInfoDto findSoloRankInfo(List<Map<String, Object>> rankList, String queueType) {
        return rankList.stream()
                .filter(rank -> queueType.equals(rank.get("queueType")))
                .findFirst()
                .map(RankInfoDto::from)
                .orElseGet(RankInfoDto::unranked);
    }

    private RiotAPIException handleApiError(Exception e) {
        // RestClient의 모든 HTTP 예외는 RestClientResponseException의 하위 클래스
        if (e instanceof RestClientResponseException restError) {
            HttpStatus status = (HttpStatus) restError.getStatusCode();
            String responseBody = restError.getResponseBodyAsString();

            log.error("RestClient 응답 오류 - 상태: {}, 응답: {}", status, responseBody);

            return switch (status) {
                case NOT_FOUND -> new RiotAPIException("사용자 정보가 없습니다.", ErrorCode.API_ACCESS_ERROR);
                case UNAUTHORIZED -> new RiotAPIException("유효하지 않은 API_KEY", ErrorCode.API_ACCESS_ERROR);
                case FORBIDDEN -> new RiotAPIException("API 접근 권한이 없습니다.", ErrorCode.API_ACCESS_ERROR);
                case TOO_MANY_REQUESTS -> new RiotAPIException("API 요청 한도를 초과했습니다.", ErrorCode.API_ACCESS_ERROR);
                default -> new RiotAPIException("Riot API 응답 오류: " + status, ErrorCode.API_ACCESS_ERROR);
            };
        }

        log.error("예상치 못한 API 오류", e);
        return new RiotAPIException("API 호출 실패", ErrorCode.API_ACCESS_ERROR);
    }
}
