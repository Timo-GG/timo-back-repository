package com.tools.seoultech.timoproject.riot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.riot.dto.Detail_MatchInfoDTO;
import com.tools.seoultech.timoproject.riot.dto.MatchInfoDTO;
import com.tools.seoultech.timoproject.global.exception.RiotAPIException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
@Validated
@Slf4j
public class BasicAPIService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private HttpRequest request;
    private HttpResponse<String> response;

    @Value("${api_key}") private String api_key;

    // URL 인코딩 메서드
    private String encodeUrlParameter(String param) {
        try {
            return java.net.URLEncoder.encode(param, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20"); // 공백을 %20으로 변환
        } catch (Exception e) {
            throw new RuntimeException("URL 인코딩 실패: " + param, e);
        }
    }

    @Transactional
    public AccountDto.Response findUserAccount(@Valid AccountDto.Request dto) throws Exception {
        String encodedGameName = encodeUrlParameter(dto.getGameName());
        String encodedTagLine = encodeUrlParameter(dto.getTagLine());

        String url = "https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/"
                + encodedGameName + "/" + encodedTagLine;

        log.info("API URL: " + url);

        request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Riot-Token", api_key) // ✅ 필수
                .GET()
                .build();

        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        riotAPI_validation(response);
        return mapper.readValue(response.body(), AccountDto.Response.class);
    }


    private void riotAPI_validation(HttpResponse<String> response) {
        System.out.println("Response Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            log.info("riot API 예외 처리 - 사용자를 찾을 수 없습니다.");
            throw new RiotAPIException("계정 정보 API 호출 실패 - 사용자 정보가 없습니다.", ErrorCode.API_ACCESS_ERROR);
        }
        if (response.statusCode() == HttpStatus.UNAUTHORIZED.value()) {
            log.info("riot API 예외 처리 - API_KEY가 유효하지 않습니다.");
            throw new RiotAPIException("유효하지 않은 API_KEY", ErrorCode.API_ACCESS_ERROR);
        }
    }

    public MatchInfoDTO requestMatchInfoRaw(String matchid) throws Exception {
        String encodedMatchId = encodeUrlParameter(matchid);

        StringBuilder sb = new StringBuilder();
        sb.append("https://asia.api.riotgames.com/lol/match/v5/matches/")
                .append(encodedMatchId);

        request = HttpRequest.newBuilder()
                .uri(URI.create(sb.toString()))
                .header("X-Riot-Token", api_key)
                .GET()
                .build();
        response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        System.err.println("Service: " + response);
        System.err.println(response.body());
        return MatchInfoDTO.of(response.body());
    }

    public Detail_MatchInfoDTO requestMatchInfo(String matchid, String my_puuid, String requestRuneDataString) throws Exception {
        String encodedMatchId = encodeUrlParameter(matchid);

        StringBuilder sb = new StringBuilder();
        sb.append("https://asia.api.riotgames.com/lol/match/v5/matches/")
                .append(encodedMatchId);

        request = HttpRequest.newBuilder()
                .uri(URI.create(sb.toString()))
                .header("X-Riot-Token", api_key)
                .GET()
                .build();
        response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        log.info("BasicAPIService: Completed MatchInfoDTO request");
        MatchInfoDTO matchInfoDTO = MatchInfoDTO.of(response.body());

        log.info("BasicAPIService: Completed Detail_MatchInfoDTO request");
        return Detail_MatchInfoDTO.of(matchInfoDTO, my_puuid, requestRuneDataString);
    }

    public String requestRuneData() throws Exception {
        request = HttpRequest.newBuilder()
                .uri(URI.create("https://ddragon.leagueoflegends.com/cdn/14.23.1/data/en_US/runesReforged.json"))
                .GET()
                .build();
        response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString());
        log.info("BasicAPIService: Completed RuneData request");
        return response.body();
    }

    public List<String> requestMatchList(String puuid) throws Exception {
        String encodedPuuid = encodeUrlParameter(puuid);

        StringBuilder sb = new StringBuilder();
        sb.append("https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/")
                .append(encodedPuuid)
                .append("/ids");

        request = HttpRequest.newBuilder()
                .uri(UriComponentsBuilder.fromUriString(sb.toString())
                        .queryParam("start", 0)
                        .queryParam("count", 20)
                        .queryParam("api_key", api_key)
                        .build().toUri())
                .GET()
                .build();
        response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString());
        log.info("BasicAPIService: Completed MatchList request");
        return mapper.readValue(response.body(), List.class);
    }
}

