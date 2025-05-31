package com.tools.seoultech.timoproject.matching.controller;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.service.facade.BoardFacade;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
@Tag(name = "Matching", description = "Matching API")
public class BoardController{
    private final BoardFacade boardFacade;

    @GetMapping("/duo/exists")
    public ResponseEntity<APIDataResponse<Boolean>> checkMyDuoBoardExists(@CurrentMemberId Long memberId) {
        boolean exists = boardFacade.existsByMemberId(memberId);
        return ResponseEntity.ok(APIDataResponse.of(exists));
    }

    @PutMapping("/duo/refresh")
    public ResponseEntity<APIDataResponse<BoardDTO.Response>> refreshMyDuoBoard(@CurrentMemberId Long memberId) throws Exception {
        var response = boardFacade.refreshMyDuoBoard(memberId);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.OK);
    }

    /** [Create] 게시글 생성 */
    @PostMapping("/duo")
    public ResponseEntity<APIDataResponse<BoardDTO.Response>> createDuoBoard(@RequestBody BoardDTO.RequestDuo dto) {
        // TODO: memberAccount에 riotAccount 등록 유뮤 확인하는 로직 추가 필요.
        var response = boardFacade.create(dto);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.CREATED);
    }

    @PostMapping("/scrim")
    public ResponseEntity<APIDataResponse<BoardDTO.Response>> createScrimBoard(@RequestBody BoardDTO.RequestScrim dto) {
        var response = boardFacade.create(dto);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.CREATED);
    }

    /** [Read] 모든 게시글 조회 */
    @GetMapping("/duo")
    public ResponseEntity<APIDataResponse<BoardDTO.PageResponse>> getAllDuoBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {
        var response = boardFacade.readAllWithPaging(MatchingCategory.DUO, page, size);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.OK);
    }


    @GetMapping("/scrim")
    public ResponseEntity<APIDataResponse<BoardDTO.PageResponse>> getAllScrimBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws Exception {
        var response = boardFacade.readAllWithPaging(MatchingCategory.SCRIM, page, size);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.OK);
    }

    /** [Read] 게시글 UUID 조회 */
    @GetMapping("/duo/{boardUUID}")
    public ResponseEntity<APIDataResponse<BoardDTO.Response>> getDuoBoard(@PathVariable UUID boardUUID) throws Exception {
        var response = boardFacade.read(boardUUID);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.OK);
    }

    @GetMapping("/scrim/{boardUUID}")
    public ResponseEntity<APIDataResponse<BoardDTO.Response>> getScrimBoard(@PathVariable UUID boardUUID) throws Exception {
        var response = boardFacade.read(boardUUID);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.OK);
    }

    /** [Update] 게시글 정보 수정 */
    @PutMapping("/duo")
    public ResponseEntity<APIDataResponse<BoardDTO.Response>> updateDuoBoard(@RequestBody BoardDTO.RequestUpdateDuo dto) throws Exception {
        var response = boardFacade.update(dto);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.OK);
    }

    @PutMapping("/scrim")
    public ResponseEntity<APIDataResponse<BoardDTO.Response>> updateDuoBoard(@RequestBody BoardDTO.RequestUpdateScrim dto) throws Exception {
        var response = boardFacade.update(dto);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.OK);
    }

    /**  [Delete] 게시글 UUID 삭제 */
    @DeleteMapping("/{boardUUID}")
    public ResponseEntity<Void> deleteBoard(@PathVariable UUID boardUUID) throws Exception {
        boardFacade.delete(boardUUID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /** [Delete] 모든 게시글 삭제 */
    @DeleteMapping("/duo")
    public ResponseEntity<Void> deleteAllDuoBoards() throws Exception{
        boardFacade.deleteAll(MatchingCategory.DUO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/scrim")
    public ResponseEntity<Void> deleteAllScrimBoards() {
        boardFacade.deleteAll(MatchingCategory.SCRIM);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/duo/my")
    public ResponseEntity<Void> deleteMyDuoBoard(@CurrentMemberId Long memberId) throws Exception {
        boardFacade.deleteByMemberId(memberId, MatchingCategory.DUO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/scrim/my")
    public ResponseEntity<Void> deleteMyScrimBoard(@CurrentMemberId Long memberId) throws Exception {
        boardFacade.deleteByMemberId(memberId, MatchingCategory.SCRIM);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
