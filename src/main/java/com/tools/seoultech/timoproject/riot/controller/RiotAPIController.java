package com.tools.seoultech.timoproject.riot.controller;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.riot.dto.*;
import com.tools.seoultech.timoproject.member.dto.AccountDto;

import com.tools.seoultech.timoproject.global.exception.RiotAPIException;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/riot")
@Validated
@Tag(name = "RiotAPI", description = "Riot API")
public class RiotAPIController {
    private final RiotAPIService bas;

    @GetMapping("/Account")
    public ResponseEntity<APIDataResponse<AccountDto.Response>> requestAccount(
            @Valid AccountDto.Request dto) throws RiotAPIException, Exception {
        AccountDto.Response response_dto = bas.findUserAccount(dto);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(response_dto));
    }

    @GetMapping("/MatchV5/matches/matchList")
    public ResponseEntity<APIDataResponse<List<String>>> requestMatchList(
            @Valid AccountDto.Request dto) throws RiotAPIException, Exception {
        AccountDto.Response response_dto = bas.findUserAccount(dto);
        List<String> matchList = bas.requestMatchList(response_dto.getPuuid());
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(matchList));
    }

    @GetMapping("/MatchV5/matches/matchInfoDTO")
    public ResponseEntity<APIDataResponse<List<MatchInfoDTO>>> requestMatchV5(
            @Valid AccountDto.Request dto) throws RiotAPIException, Exception {
        String puuid = bas.findUserAccount(dto).getPuuid();
        List<String> matchList = bas.requestMatchList(puuid);
        List<MatchInfoDTO> dto_List = matchList.parallelStream()
                .map(matchId -> {
                    try {
                        return bas.requestMatchInfoRaw(matchId);
                    } catch (Exception e) {
                        log.error("매치 정보 조회 실패: {}", matchId, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(dto_List));
    }

    @GetMapping("/MatchV5/matches/전적검색")
    public ResponseEntity<APIDataResponse<List<DetailMatchInfoDTO>>> requestMatchInfo(
            @Valid AccountDto.Request dto) throws Exception {
        String puuid = bas.findUserAccount(dto).getPuuid();
        List<String> matchList = bas.requestMatchList(puuid);
        String runeData = bas.requestRuneData();

        List<DetailMatchInfoDTO> dto_List = matchList.parallelStream()
                .map(matchId -> {
                    try {
                        return bas.requestMatchInfo(matchId, puuid, runeData);
                    } catch (Exception e) {
                        log.error("상세 매치 정보 조회 실패: {}", matchId, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(dto_List));
    }

    @GetMapping("/most-champ/{puuid}")
    public ResponseEntity<APIDataResponse<List<String>>> requestMostChamp(
            @PathVariable String puuid) throws Exception {
        List<String> mostChamp = bas.getMost3ChampionNames(puuid);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(mostChamp));
    }

    /** 최근 전적 10개 가져오기 - 병렬 처리로 성능 최적화 */
    @PostMapping("/recent-match")
    public ResponseEntity<APIDataResponse<RecentMatchFullResponse>> requestRecentMatch(
            @RequestBody AccountDto.Request request) throws Exception {

        AccountDto.Response account = bas.findUserAccount(request);
        String puuid = account.getPuuid();

        // 병렬 처리로 성능 개선된 메서드 사용
        List<MatchSummaryDTO> recentMatch = bas.getRecentMatchSummaries(puuid);
        RankInfoDto rankInfo = bas.getSoloRankInfoByPuuid(puuid);
        String profileIconUrl = bas.getProfileIconUrlByPuuid(puuid);

        RecentMatchFullResponse response = new RecentMatchFullResponse(
                account.getGameName(),
                account.getTagLine(),
                profileIconUrl,
                rankInfo,
                recentMatch
        );

        return ResponseEntity.ok(APIDataResponse.of(response));
    }

    @PostMapping("/compactHistory")
    public ResponseEntity<APIDataResponse<CompactPlayerHistory>> requestCompactPlayerHistory(
            @RequestBody AccountDto.Request request) throws Exception {
        AccountDto.Response account = bas.findUserAccount(request);
        String puuid = account.getPuuid();

        RankInfoDto rankInfo = bas.getSoloRankInfoByPuuid(puuid);
        List<String> most3Champion = bas.getMost3ChampionNames(puuid);
        List<MatchSummaryDTO> recentMatch = bas.getRecentMatchSummaries(puuid);

        CompactPlayerHistory response = new CompactPlayerHistory(rankInfo, most3Champion, recentMatch);
        return ResponseEntity.ok(APIDataResponse.of(response));
    }

    @GetMapping("/rank-info/{puuid}")
    public ResponseEntity<APIDataResponse<RankInfoDto>> requestRankInfo(
            @PathVariable String puuid) throws Exception {
        RankInfoDto rankInfo = bas.getSoloRankInfoByPuuid(puuid);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(rankInfo));
    }

    @GetMapping("/image/{puuid}")
    public ResponseEntity<APIDataResponse<String>> requestImage(
            @PathVariable String puuid) throws Exception {
        String imageUrl = bas.getProfileIconUrlByPuuid(puuid);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(imageUrl));
    }
}
