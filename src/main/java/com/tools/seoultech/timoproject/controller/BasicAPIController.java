package com.tools.seoultech.timoproject.controller;

import com.tools.seoultech.timoproject.dto.APIDataResponse;
import com.tools.seoultech.timoproject.dto.AccountDto;

import com.tools.seoultech.timoproject.dto.Detail_MatchInfoDTO;
import com.tools.seoultech.timoproject.dto.MatchInfoDTO;
import com.tools.seoultech.timoproject.exception.RiotAPIException;
import com.tools.seoultech.timoproject.service.BasicAPIService;
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
}
