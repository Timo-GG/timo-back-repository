package com.tools.seoultech.timoproject.riot.controller;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.riot.dto.*;
import com.tools.seoultech.timoproject.member.dto.AccountDto;

import com.tools.seoultech.timoproject.global.exception.RiotAPIException;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/riot")
@Validated
@Tag(name = "RiotAPI", description = "Riot API")
public class RiotAPIController {
    private final RiotAPIService bas;

    @GetMapping("/Account")
    public ResponseEntity<APIDataResponse<AccountDto.Response>> requestAccount(
            @Valid AccountDto.Request dto) throws RiotAPIException, Exception
    {
        AccountDto.Response response_dto = bas.findUserAccount(dto);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(response_dto));
    }
    @GetMapping("/MatchV5/matches/matchList")
    public ResponseEntity<APIDataResponse<List<String>>> requestMatchList(
            @Valid AccountDto.Request dto) throws RiotAPIException, Exception{
        AccountDto.Response response_dto = bas.findUserAccount(dto);
        List<String> matchList = bas.requestMatchList(response_dto.getPuuid());
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(matchList));
    }

    @GetMapping("/MatchV5/matches/matchInfoDTO")
    public ResponseEntity<APIDataResponse<List<MatchInfoDTO>>> requestMatchV5(
            @Valid AccountDto.Request dto
    )throws RiotAPIException, Exception{
        String puuid = bas.findUserAccount(dto).getPuuid();
        List<String> matchList = bas.requestMatchList(puuid);
        List<MatchInfoDTO> dto_List = Collections.synchronizedList(new ArrayList<>());
        matchList.stream().parallel()
                .forEachOrdered((matchId) -> {
                    try {
                        MatchInfoDTO info = bas.requestMatchInfoRaw(matchId);
                        dto_List.add(info);
                    }catch (Exception e) {throw new RiotAPIException("컨트롤러: requestMatchInfoRaw 중 오류발생.");}
                });
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(dto_List));
    }
    @GetMapping("/MatchV5/matches/전적검색")
    public ResponseEntity<APIDataResponse<List<DetailMatchInfoDTO>>> requestMatchInfo(
            @Valid AccountDto.Request dto) throws Exception{
        String puuid = bas.findUserAccount(dto).getPuuid();
        List<String> matchList = bas.requestMatchList(puuid);
        List<DetailMatchInfoDTO> dto_List = Collections.synchronizedList(new ArrayList<>());

        String subString = bas.requestRuneData();
        matchList.stream().parallel()
                .forEachOrdered( matchId -> {
                       try{
                           DetailMatchInfoDTO dto_detail = bas.requestMatchInfo(matchId, puuid, subString);
                           dto_List.add(dto_detail);
                       } catch(Exception e){ throw new RiotAPIException("Detail_matchInfo(matchId)중 오류 발생.");}
                });
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(dto_List));
    }

    @GetMapping("/most-champ/{puuid}")
    public ResponseEntity<APIDataResponse<List<String>>> requestMostChamp(
            @PathVariable String puuid) throws Exception{
        List<String> mostChamp = bas.getMost3ChampionNames(puuid);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(mostChamp));
    }

    /** 최근 전적 10개 가져오기 */
    @PostMapping("/recent-match")
    public ResponseEntity<APIDataResponse<RecentMatchFullResponse>> requestRecentMatch(
            @RequestBody AccountDto.Request request) throws Exception {

        AccountDto.Response account = bas.findUserAccount(request);
        String puuid = account.getPuuid();

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

    /** 소환사 티어 정보 가져오기 */
    @GetMapping("/rank-info/{puuid}")
    public ResponseEntity<APIDataResponse<RankInfoDto>> requestRankInfo(
            @PathVariable String puuid) throws Exception {

        RankInfoDto rankInfo = bas.getSoloRankInfoByPuuid(puuid); // 서비스에서 처리
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(rankInfo));
    }

    @GetMapping("/image/{puuid}")
    public ResponseEntity<APIDataResponse<String>> requestImage(
            @PathVariable String puuid) throws Exception {
        String imageUrl = bas.getProfileIconUrlByPuuid(puuid);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(imageUrl));
    }
}
