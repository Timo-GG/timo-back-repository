package com.tools.seoultech.timoproject.riot.controller;

import com.tools.seoultech.timoproject.riot.dto.*;
import com.tools.seoultech.timoproject.memberAccount.dto.AccountDto;

import com.tools.seoultech.timoproject.global.exception.RiotAPIException;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
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
@RequestMapping("/api")
@Validated
public class BasicAPIController {
    private final BasicAPIService bas;

    @GetMapping("/request/Account")
    public ResponseEntity<APIDataResponse<AccountDto.Response>> requestAccount(
            @Valid AccountDto.Request dto) throws RiotAPIException, Exception
    {
        AccountDto.Response response_dto = bas.findUserAccount(dto);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(response_dto));
    }
    @GetMapping("/request/MatchV5/matches/matchList")
    public ResponseEntity<APIDataResponse<List<String>>> requestMatchList(
            @Valid AccountDto.Request dto) throws RiotAPIException, Exception{
        AccountDto.Response response_dto = bas.findUserAccount(dto);
        List<String> matchList = bas.requestMatchList(response_dto.getPuuid());
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(matchList));
    }

    @GetMapping("/request/MatchV5/matches/matchInfoDTO")
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
    @GetMapping("/request/MatchV5/matches/전적검색")
    public ResponseEntity<APIDataResponse<List<Detail_MatchInfoDTO>>> requestMatchInfo(
            @Valid AccountDto.Request dto) throws Exception{
        String puuid = bas.findUserAccount(dto).getPuuid();
        List<String> matchList = bas.requestMatchList(puuid);
        List<Detail_MatchInfoDTO> dto_List = Collections.synchronizedList(new ArrayList<>());

        String subString = bas.requestRuneData();
        matchList.stream().parallel()
                .forEachOrdered( matchId -> {
                       try{
                           Detail_MatchInfoDTO dto_detail = bas.requestMatchInfo(matchId, puuid, subString);
                           dto_List.add(dto_detail);
                       } catch(Exception e){ throw new RiotAPIException("Detail_matchInfo(matchId)중 오류 발생.");}
                });
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(dto_List));
    }

    @GetMapping("/request/most-champ/{puuid}")
    public ResponseEntity<APIDataResponse<List<String>>> requestMostChamp(
            @PathVariable String puuid) throws Exception{
        List<String> mostChamp = bas.getMost3ChampionNames(puuid);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(mostChamp));
    }

    @GetMapping("/request/recent-match/{puuid}")
    public ResponseEntity<APIDataResponse<List<MatchSummaryDTO>>> requestRecentMatch(
            @PathVariable String puuid) throws Exception{
        List<MatchSummaryDTO> recentMatch = bas.getRecentMatchSummaries(puuid);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(recentMatch));
    }

    @GetMapping("/request/rank-info/{puuid}")
    public ResponseEntity<APIDataResponse<RankInfoDto>> requestRankInfo(
            @PathVariable String puuid) throws Exception {

        RankInfoDto rankInfo = bas.getSoloRankInfoByPuuid(puuid); // 서비스에서 처리
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(rankInfo));
    }

    @GetMapping("/request/image/{puuid}")
    public ResponseEntity<APIDataResponse<String>> requestImage(
            @PathVariable String puuid) throws Exception {
        String imageUrl = bas.getProfileIconUrlByPuuid(puuid);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(imageUrl));
    }
}
