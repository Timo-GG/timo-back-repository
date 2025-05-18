package com.tools.seoultech.timoproject.riot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.riot.dto.*;
import com.tools.seoultech.timoproject.global.exception.RiotAPIException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class BasicAPIService {
    private static final String BASE_API_URL = "https://asia.api.riotgames.com";
    private static final String KR_API_URL = "https://kr.api.riotgames.com";
    private static final String DDRAGON_URL = "https://ddragon.leagueoflegends.com";
    private static final String QUEUE_TYPE_SOLO = "RANKED_SOLO_5x5";
    private static final String DDRAGON_VERSIONS_URL = DDRAGON_URL + "/api/versions.json";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ChampionCacheService championCacheService;

    @Value("${api_key}")
    private String api_key;

    private String ddragonVersion;

    @PostConstruct
    private void fetchLatestDdragonVersion() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(DDRAGON_VERSIONS_URL))
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() == HttpStatus.OK.value()) {
                List<String> versions = mapper.readValue(resp.body(), new TypeReference<List<String>>() {});
                if (!versions.isEmpty()) {
                    ddragonVersion = versions.get(0);
                    log.info("DataDragon 최신 버전: {}", ddragonVersion);
                    return;
                }
            }
            log.warn("DataDragon 버전 리스트 응답 이상: status={}", resp.statusCode());
        } catch (Exception e) {
            log.error("DataDragon 버전 조회 실패", e);
        }
        // 실패 시 fallback
        ddragonVersion = "14.23.1";
        log.info("DataDragon 버전 fallback: {}", ddragonVersion);
    }

    // URL 인코딩 메서드
    private String encodeUrlParameter(String param) {
        try {
            return java.net.URLEncoder.encode(param, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20"); // 공백을 %20으로 변환
        } catch (Exception e) {
            log.error("URL 인코딩 실패: {}", param, e);
            throw new RuntimeException("URL 인코딩 실패: " + param, e);
        }
    }

    @Transactional
    public AccountDto.Response findUserAccount(@Valid AccountDto.Request dto) {
        try {
            String encodedGameName = encodeUrlParameter(dto.getGameName());
            String encodedTagLine = encodeUrlParameter(dto.getTagLine());

            String url = BASE_API_URL + "/riot/account/v1/accounts/by-riot-id/"
                    + encodedGameName + "/" + encodedTagLine;

            log.info("API URL: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-Riot-Token", api_key)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            riotAPI_validation(response);
            return mapper.readValue(response.body(), AccountDto.Response.class);
        } catch (IOException | InterruptedException e) {
            log.error("라이엇 API 호출 중 예외 발생", e);
            throw new RiotAPIException("라이엇 계정 조회 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    private void riotAPI_validation(HttpResponse<String> response) {
        log.debug("Response Status Code: {}", response.statusCode());
        log.debug("Response Body: {}", response.body());

        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            log.info("riot API 예외 처리 - 사용자를 찾을 수 없습니다.");
            throw new RiotAPIException("계정 정보 API 호출 실패 - 사용자 정보가 없습니다.", ErrorCode.API_ACCESS_ERROR);
        }
        if (response.statusCode() == HttpStatus.UNAUTHORIZED.value()) {
            log.info("riot API 예외 처리 - API_KEY가 유효하지 않습니다.");
            throw new RiotAPIException("유효하지 않은 API_KEY", ErrorCode.API_ACCESS_ERROR);
        }
        if (response.statusCode() != HttpStatus.OK.value()) {
            log.error("Riot API 응답 오류 - 상태 코드: {}", response.statusCode());
            throw new RiotAPIException("Riot API 응답 오류", ErrorCode.API_ACCESS_ERROR);
        }
    }

    public MatchInfoDTO requestMatchInfoRaw(String matchid) {
        try {
            String encodedMatchId = encodeUrlParameter(matchid);
            String url = BASE_API_URL + "/lol/match/v5/matches/" + encodedMatchId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-Riot-Token", api_key)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            riotAPI_validation(response);

            log.info("매치 정보 API 응답 받음: {}", matchid);
            return MatchInfoDTO.of(response.body());
        } catch (IOException | InterruptedException e) {
            log.error("매치 정보 조회 중 예외 발생", e);
            throw new RiotAPIException("매치 정보 조회 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    public Detail_MatchInfoDTO requestMatchInfo(String matchid, String my_puuid, String requestRuneDataString) {
        try {
            MatchInfoDTO matchInfoDTO = requestMatchInfoRaw(matchid);
            log.info("상세 매치 정보 변환 시작: {}", matchid);
            return Detail_MatchInfoDTO.of(matchInfoDTO, my_puuid, requestRuneDataString);
        } catch (Exception e) {
            log.error("상세 매치 정보 변환 중 예외 발생", e);
            throw new RiotAPIException("상세 매치 정보 변환 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    public String requestRuneData() {
        try {
            String url = DDRAGON_URL + "/cdn/14.23.1/data/en_US/runesReforged.json";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                log.error("룬 데이터 API 응답 오류 - 상태 코드: {}", response.statusCode());
                throw new RiotAPIException("룬 데이터 API 응답 오류", ErrorCode.API_ACCESS_ERROR);
            }

            log.info("룬 데이터 API 응답 받음");
            return response.body();
        } catch (IOException | InterruptedException e) {
            log.error("룬 데이터 조회 중 예외 발생", e);
            throw new RiotAPIException("룬 데이터 조회 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    public List<String> requestMatchList(String puuid) {
        try {
            String encodedPuuid = encodeUrlParameter(puuid);
            String baseUrl = BASE_API_URL + "/lol/match/v5/matches/by-puuid/" + encodedPuuid + "/ids";

            URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                    .queryParam("start", 0)
                    .queryParam("count", 20)
                    .build()
                    .toUri();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("X-Riot-Token", api_key)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            riotAPI_validation(response);

            log.info("매치 목록 API 응답 받음: {}", puuid);
            return mapper.readValue(response.body(), List.class);
        } catch (IOException | InterruptedException e) {
            log.error("매치 목록 조회 중 예외 발생", e);
            throw new RiotAPIException("매치 목록 조회 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    public List<String> getMost3ChampionNames(String puuid) {
        try {
            String url = KR_API_URL + "/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-Riot-Token", api_key)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            riotAPI_validation(response);

            List<Map<String, Object>> masteryList = mapper.readValue(response.body(), List.class);
            Map<Integer, String> champMap = championCacheService.getChampionIdToNameMap();

            log.info("챔피언 숙련도 API 응답 받음: {}", puuid);
            return masteryList.stream()
                    .sorted((a, b) -> Integer.compare((int) b.get("championPoints"), (int) a.get("championPoints")))
                    .limit(3)
                    .map(champ -> {
                        Integer champId = (Integer) champ.get("championId");
                        return champMap.getOrDefault(champId, "Unknown");
                    })
                    .toList();
        } catch (IOException | InterruptedException e) {
            log.warn("챔피언 숙련도 조회 실패, 빈 리스트 반환", e);
            return Collections.emptyList();
        }
    }

    @Transactional
    public List<MatchSummaryDTO> getRecentMatchSummaries(String puuid) {
        try {
            List<String> matchIds = requestMatchList(puuid).stream().limit(10).toList();
            if (matchIds.isEmpty()) {
                log.info("최근 매치 정보가 없습니다: {}", puuid);
                return Collections.emptyList();
            }

            List<MatchSummaryDTO> summaries = new ArrayList<>();
            String runeData = requestRuneData();

            for (String matchId : matchIds) {
                try {
                    Detail_MatchInfoDTO detail = requestMatchInfo(matchId, puuid, runeData);

                    MatchSummaryDTO summary = MatchSummaryDTO.builder()
                            .gameDuration(detail.getTime())
                            .playedAt(detail.getLastGameEnd())
                            .gameMode(detail.getMode())
                            .championName(detail.getMyName())
                            .championIconUrl(detail.getIcon())
                            .championLevel(detail.getChampionLevel() != null ? detail.getChampionLevel() : 0)
                            .kills(detail.getKills())
                            .deaths(detail.getDeaths())
                            .assists(detail.getAssists())
                            .isWin(detail.getWin())
                            .minionsPerMinute(detail.getMinionskilledPerMin())
                            .runes(List.of(detail.getRune3(), detail.getRune4()))
                            .summonerSpells(List.of(detail.getSummoner1Id(), detail.getSummoner2Id()))
                            .items(detail.getItems())
                            .build();

                    summaries.add(summary);
                } catch (Exception e) {
                    log.error("매치 요약 정보 생성 중 오류 발생: {}", matchId, e);
                    // 개별 매치 오류는 무시하고 계속 진행
                }
            }

            log.info("최근 매치 요약 정보 생성 완료: {} 개 매치", summaries.size());
            return summaries;
        } catch (Exception e) {
            log.error("최근 매치 요약 정보 생성 중 예외 발생", e);
            throw new RiotAPIException("최근 매치 요약 정보 생성 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    public WinLossSummaryDto getRecentWinLossSummary(String puuid) {
        try {
            List<String> matchIds = requestMatchList(puuid).stream().limit(10).toList();
            if (matchIds.isEmpty()) {
                log.info("최근 매치가 없습니다: {}", puuid);
                return WinLossSummaryDto.builder().wins(0).losses(0).build();
            }

            int wins = 0;
            int losses = 0;

            for (String matchId : matchIds) {
                try {
                    MatchInfoDTO matchInfoDTO = requestMatchInfoRaw(matchId);

                    boolean didWin = matchInfoDTO.getMyInfo(puuid).getWin();
                    if (didWin) wins++;
                    else losses++;

                } catch (Exception e) {
                    log.warn("매치 승패 확인 실패: {}", matchId, e);
                }
            }

            return WinLossSummaryDto.builder()
                    .wins(wins)
                    .losses(losses)
                    .build();

        } catch (Exception e) {
            log.error("최근 승/패 요약 조회 실패", e);
            throw new RiotAPIException("최근 승패 정보 조회 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    public RankInfoDto getSoloRankInfoByPuuid(String puuid) {
        try {
            URI uri = URI.create(KR_API_URL + "/lol/league/v4/entries/by-puuid/" + puuid);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("X-Riot-Token", api_key)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            riotAPI_validation(response);
            log.info("랭크 정보 API 응답 받음: {}", puuid);

            List<Map<String, Object>> rankList = mapper.readValue(response.body(), new TypeReference<>() {
            });
            return findSoloRankInfo(rankList, QUEUE_TYPE_SOLO);
        } catch (IOException | InterruptedException e) {
            log.error("랭크 정보 조회 중 예외 발생", e);
            throw new RiotAPIException("랭크 정보 조회 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    public String getProfileIconUrlByPuuid(String puuid) {
        try {
            String url = KR_API_URL + "/lol/summoner/v4/summoners/by-puuid/" + puuid;
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-Riot-Token", api_key)
                    .GET()
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            riotAPI_validation(resp);

            Map<String,Object> data = mapper.readValue(resp.body(), new TypeReference<>() {});
            Integer iconId = (Integer)data.get("profileIconId");
            if (iconId == null) {
                log.warn("profileIconId 누락: {}", puuid);
                return null;
            }
            // 캐싱된 최신 버전 사용
            return DDRAGON_URL + "/cdn/" + ddragonVersion + "/img/profileicon/" + iconId + ".png";

        } catch (IOException | InterruptedException e) {
            log.error("프로필 이미지 URL 생성 실패", e);
            throw new RiotAPIException("프로필 이미지 조회 실패", ErrorCode.API_ACCESS_ERROR);
        }
    }

    private RankInfoDto findSoloRankInfo(List<Map<String, Object>> rankList, String queueType) {
        return rankList.stream()
                .filter(rank -> queueType.equals(rank.get("queueType")))
                .findFirst()
                .map(this::buildRankInfoFrom)
                .orElse(buildUnrankedInfo());
    }

    private RankInfoDto buildRankInfoFrom(Map<String, Object> rank) {
        return RankInfoDto.builder()
                .tier((String) rank.get("tier"))
                .rank((String) rank.get("rank"))
                .lp((Integer) rank.get("leaguePoints"))
                .wins((Integer) rank.get("wins"))
                .losses((Integer) rank.get("losses"))
                .build();
    }

    private RankInfoDto buildUnrankedInfo() {
        return RankInfoDto.builder()
                .tier("UNRANKED")
                .rank("")
                .lp(0)
                .wins(0)
                .losses(0)
                .build();
    }
}