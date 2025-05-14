package com.tools.seoultech.timoproject.matching.controller;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.service.BoardService;
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
public class BoardController {

    private final BoardService boardService;

    /**
     * Duo 게시판에 게시글을 추가
     */
    @PostMapping("/duo")
    public ResponseEntity<RedisBoard.Duo> createDuoBoard(@RequestBody BoardDTO.RequestDuo requestDuo) {
        System.err.println("Board Controller @Post");
        // TODO: memberAccount에 riotAccount 등록 유뮤 확인하는 로직 추가 필요.
        RedisBoard.Duo savedBoard = boardService.saveDuoBoard(requestDuo);
        return new ResponseEntity<>(savedBoard, HttpStatus.CREATED);
    }

    /**
     * Colosseum 게시판에 게시글을 추가
     */
    @PostMapping("/colosseum")
    public ResponseEntity<RedisBoard.Colosseum> createColosseumBoard(@RequestBody BoardDTO.RequestScrim requestScrim) {
        RedisBoard.Colosseum savedBoard = boardService.saveColosseumBoard(requestScrim);
        return new ResponseEntity<>(savedBoard, HttpStatus.CREATED);
    }

    /**
     * 모든 Duo 게시글 조회
     */
    @GetMapping("/duo")
    public ResponseEntity<List<BoardDTO.ResponseDuo>> getAllDuoBoards() {
        List<BoardDTO.ResponseDuo> boards = boardService.getAllDuoBoards();
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    /**
     * 모든 Colosseum 게시글 조회
     */
    @GetMapping("/colosseum")
    public ResponseEntity<List<BoardDTO.ResponseScrim>> getAllColosseumBoards() {
        List<BoardDTO.ResponseScrim> boards = boardService.getAllColosseumBoards();
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    /**
     * 특정 UUID의 Duo 게시글 조회
     */
    @GetMapping("/duo/{boardUUID}")
    public ResponseEntity<BoardDTO.ResponseDuo> getDuoBoard(@PathVariable UUID boardUUID) {
        try {
            BoardDTO.ResponseDuo board = boardService.getDuoBoard(boardUUID);
            return new ResponseEntity<>(board, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 특정 UUID의 Colosseum 게시글 조회
     */
    @GetMapping("/colosseum/{boardUUID}")
    public ResponseEntity<BoardDTO.ResponseScrim> getColosseumBoard(@PathVariable UUID boardUUID) {
        try {
            BoardDTO.ResponseScrim board = boardService.getScrimBoard(boardUUID);
            return new ResponseEntity<>(board, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 특정 UUID의 게시글 삭제
     */
    @DeleteMapping("/{boardUUID}")
    public ResponseEntity<Void> deleteBoard(@PathVariable UUID boardUUID) {
        try {
            boardService.deleteBoardByUUID(boardUUID);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 모든 게시글 삭제
     */
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllBoards() {
        boardService.deleteAllBoards();
        return ResponseEntity.noContent().build();
    }

    /**
     * Duo 게시글 전체 삭제
     */
    @DeleteMapping("/duo")
    public ResponseEntity<Void> deleteAllDuoBoards() {
        boardService.deleteAllDuoBoards();
        return ResponseEntity.noContent().build();
    }

    /**ㅣ
     * Colosseum 게시글 전체 삭제
     */
    @DeleteMapping("/colosseum")
    public ResponseEntity<Void> deleteAllColosseumBoards() {
        boardService.deleteAllColosseumBoards();
        return ResponseEntity.noContent().build();
    }
}
