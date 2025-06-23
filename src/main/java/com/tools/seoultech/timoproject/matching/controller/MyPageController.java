package com.tools.seoultech.timoproject.matching.controller;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
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

    /**
     * Redis 마이페이지
     * */

    /** READ */
    @GetMapping("/mypage/duo")
    public ResponseEntity<APIDataResponse<List<MatchingDTO.Response>>> readDuoBoard() throws Exception{
        var dtoList = myPageFacade.readAllMyPage(MatchingCategory.DUO);
        return ResponseEntity.ok(APIDataResponse.of(dtoList));
    }

    @GetMapping("/mypage/acceptor/{acceptorId}")
    public ResponseEntity<APIDataResponse<List<MatchingDTO.Response>>> readAllAcceptorPage(@PathVariable Long acceptorId) throws Exception{
        var dtoList = myPageFacade.readAllMyAcceptor(acceptorId);
        return ResponseEntity.ok(APIDataResponse.of(dtoList));
    }

    @GetMapping("/mypage/requestor/{requestorId}")
    public ResponseEntity<APIDataResponse<List<MatchingDTO.Response>>> readAllRequestorPage(@PathVariable Long requestorId) throws Exception {
        var dtoList = myPageFacade.readAllMyRequestor(requestorId);
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

    @GetMapping("/mypage/exists/{boardUUID}")
    public ResponseEntity<APIDataResponse<Boolean>> checkAlreadyApplied(
            @CurrentMemberId Long memberId,
            @PathVariable UUID boardUUID) throws Exception {
        boolean exists = myPageFacade.existsPageBy(memberId, boardUUID);
        return ResponseEntity.ok(APIDataResponse.of(exists));
    }

    /** Update */
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

    /** Delete */
    // delete All
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

    // delete by uuid
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

    /**
     * MySQL 마이페이지
     * */
    @GetMapping("/db")
    public ResponseEntity<APIDataResponse<List<MyPageDTO.Response>>> readAllMyPage() throws Exception{
        var entityList = myPageFacade.readAllPage();
        return ResponseEntity.ok(APIDataResponse.of(entityList));
    }

    @GetMapping("/db/{mypageId}")
    public ResponseEntity<APIDataResponse<MyPageDTO.Response>> readPage(@PathVariable Long mypageId) throws Exception{
        var myPage = myPageFacade.readMyPage(mypageId);
        return ResponseEntity.ok(APIDataResponse.of(myPage));
    }

    @GetMapping("/db/review")
    public ResponseEntity<APIDataResponse<List<MyPageDTO.ResponseMyPage>>> readMyPage(@CurrentMemberId Long memberId) throws Exception{
        var dtoList = myPageFacade.readMyPageByMemberId(memberId);
        return ResponseEntity.ok(APIDataResponse.of(dtoList));
    }

    @PostMapping("/db/{mypageUUID}")
    public ResponseEntity<APIDataResponse<MyPage>> createPage(@PathVariable UUID mypageUUID) throws Exception {
        var entity = myPageFacade.createPage(mypageUUID);
        return ResponseEntity.ok(APIDataResponse.of(entity));
    }

    @DeleteMapping("/db")
    public ResponseEntity<Void> deleteAllPage() throws Exception {
        myPageFacade.deleteAllPage();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/db/{mypageId}")
    public ResponseEntity<Void> deletePage(@PathVariable Long mypageId) throws Exception {
        myPageFacade.delete(mypageId);
        return ResponseEntity.noContent().build();
    }

}
