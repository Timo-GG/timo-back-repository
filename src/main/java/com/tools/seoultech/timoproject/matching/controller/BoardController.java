package com.tools.seoultech.timoproject.matching.controller;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.DuoBoard;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import com.tools.seoultech.timoproject.matching.service.BoardService;
import com.tools.seoultech.timoproject.matching.service.mapper.BoardMapper;
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
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    /** 게시글 생성 */
    @PostMapping("/duo")
    public ResponseEntity<APIDataResponse<BoardDTO.ResponseDuo>> createDuoBoard(@RequestBody BoardDTO.RequestDuo dto) {
        // TODO: memberAccount에 riotAccount 등록 유뮤 확인하는 로직 추가 필요.
        BoardDTO.ResponseDuo response = boardService.saveDuoBoard(dto);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.CREATED);
    }

    @PostMapping("/colosseum")
    public ResponseEntity<APIDataResponse<BoardDTO.ResponseScrim>> createColosseumBoard(@RequestBody BoardDTO.RequestScrim requestScrim) {
        BoardDTO.ResponseScrim response = boardService.saveColosseumBoard(requestScrim);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.CREATED);
    }

    /** 모든 게시글 조회 */
    @GetMapping("/duo")
    public ResponseEntity<APIDataResponse<List<DuoBoardOnly>>> getAllDuoBoards() {
        List<DuoBoardOnly> response = boardService.getAllDuoBoards();
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.OK);
    }

    @GetMapping("/colosseum")
    public ResponseEntity<APIDataResponse<List<ScrimBoardOnly>>> getAllColosseumBoards() {
        List<ScrimBoardOnly> response = boardService.getAllColosseumBoards();
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.OK);
    }

    /** 특정 UUID 게시글 조회 */
    @GetMapping("/duo/{boardUUID}")
    public ResponseEntity<APIDataResponse<DuoBoardOnly>> getDuoBoard(@PathVariable UUID boardUUID) throws Exception {
        DuoBoardOnly response = boardService.getDuoBoard(boardUUID);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.OK);
    }

    @GetMapping("/colosseum/{boardUUID}")
    public ResponseEntity<APIDataResponse<ScrimBoardOnly>> getColosseumBoard(@PathVariable UUID boardUUID) throws Exception {
        ScrimBoardOnly response = boardService.getScrimBoard(boardUUID);
        return new ResponseEntity<>(APIDataResponse.of(response), HttpStatus.OK);
    }

    /** 특정 UUID의 게시글 삭제 */
    @DeleteMapping("/{boardUUID}")
    public ResponseEntity<Void> deleteBoard(@PathVariable UUID boardUUID) throws Exception {
        boardService.deleteDuoBoardById(boardUUID);
        boardService.deleteScrimBoardById(boardUUID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /** 모든 게시글 삭제 */
    @DeleteMapping("/all/duo")
    public ResponseEntity<Void> deleteAllDuoBoards() throws Exception{
        boardService.deleteAllDuoBoards();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all/scrim")
    public ResponseEntity<Void> deleteAllScrimBoards() {
        boardService.deleteAllColosseumBoards();
        return ResponseEntity.noContent().build();
    }
}
