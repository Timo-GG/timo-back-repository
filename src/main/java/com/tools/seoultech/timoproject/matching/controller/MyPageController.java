package com.tools.seoultech.timoproject.matching.controller;


import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.service.facade.MyPageFacade;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
@Tag(name = "Matching", description = "Matching API")
public class MyPageController {
    private final MyPageFacade myPageFacade;

    @GetMapping("/mypage/duo")
    public ResponseEntity<APIDataResponse<List<MatchingDTO.Response>>> readDuoBoard() throws Exception{
        var dtoList = myPageFacade.readAllMyPage(MatchingCategory.DUO);
        return ResponseEntity.ok(APIDataResponse.of(dtoList));
    }

    @GetMapping("/mypage/scrim")
    public ResponseEntity<APIDataResponse<List<MatchingDTO.Response>>> readScrimBoard() throws Exception{
        var dtoList = myPageFacade.readAllMyPage(MatchingCategory.SCRIM);
        return ResponseEntity.ok(APIDataResponse.of(dtoList));
    }

    @GetMapping("/mypage/duo/{myPageUUID}")
    public ResponseEntity<APIDataResponse<MatchingDTO.Response>> readDuoBoard(@PathVariable UUID myPageUUID) throws Exception{
        var dto = myPageFacade.readMyPage(myPageUUID);
        return ResponseEntity.ok(APIDataResponse.of(dto));
    }

    @GetMapping("/mypage/scrim/{myPageUUID}")
    public ResponseEntity<APIDataResponse<MatchingDTO.Response>> readScrimBoard(@PathVariable UUID myPageUUID) throws Exception{
        var dto = myPageFacade.readMyPage(myPageUUID);
        return ResponseEntity.ok(APIDataResponse.of(dto));
    }


    @PostMapping("/mypage/duo")
    public ResponseEntity<APIDataResponse<MatchingDTO.Response>> createDuoBoard(@RequestBody MatchingDTO.RequestDuo requestDto) throws Exception{
        var dto = myPageFacade.createMyPage(requestDto);
        return ResponseEntity.ok(APIDataResponse.of(dto));
    }

    @PostMapping("/mypage/scrim")
    public ResponseEntity<APIDataResponse<MatchingDTO.Response>> createScrimBoard(@RequestBody MatchingDTO.RequestScrim requestDto) throws Exception{
        var dto = myPageFacade.createMyPage(requestDto);
        return ResponseEntity.ok(APIDataResponse.of(dto));
    }

    @DeleteMapping("/mypage/duo")
    public ResponseEntity<Void> deleteAllDuoBoard() throws Exception{
        myPageFacade.deleteAllMyPage(MatchingCategory.DUO);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/mypage/scrim")
    public ResponseEntity<Void> deleteScrimBoard() throws Exception{
        myPageFacade.deleteAllMyPage(MatchingCategory.SCRIM);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/mypage/duo/{boardUUID}")
    public ResponseEntity<Void> deleteDuoBoard(@PathVariable UUID boardUUID) throws Exception{
        myPageFacade.deleteMyPage(boardUUID);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/mypage/scrim/{boardUUID}")
    public ResponseEntity<Void> deleteScrimBoard(@PathVariable UUID boardUUID) throws Exception{
        myPageFacade.deleteMyPage(boardUUID);
        return ResponseEntity.noContent().build();
    }
}
