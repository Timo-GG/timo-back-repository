package com.tools.seoultech.timoproject.matching.controller;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisMyPage;
import com.tools.seoultech.timoproject.matching.service.BoardService;
import com.tools.seoultech.timoproject.matching.service.MatchingService;
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
public class RedisController {

    private final BoardService boardService;
    private final MatchingService matchingService;

    /**
     * Duo 게시판에 게시글을 추가
     */
    @PostMapping("/duo")
    public ResponseEntity<RedisBoard.Duo> createDuoBoard(@RequestBody BoardDTO.RequestDuo requestDuo, @RequestParam UUID userUUID) {
        RedisBoard.Duo savedBoard = boardService.saveDuoBoard(requestDuo, userUUID);
        return new ResponseEntity<>(savedBoard, HttpStatus.CREATED);
    }

    /**
     * Colosseum 게시판에 게시글을 추가
     */
    @PostMapping("/colosseum")
    public ResponseEntity<RedisBoard.Colosseum> createColosseumBoard(@RequestBody BoardDTO.RequestColosseum requestColosseum, @RequestParam UUID userUUID) {
        RedisBoard.Colosseum savedBoard = boardService.saveColosseumBoard(requestColosseum, userUUID);
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
    public ResponseEntity<List<BoardDTO.ResponseColosseum>> getAllColosseumBoards() {
        List<BoardDTO.ResponseColosseum> boards = boardService.getAllColosseumBoards();
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
    public ResponseEntity<BoardDTO.ResponseColosseum> getColosseumBoard(@PathVariable UUID boardUUID) {
        try {
            BoardDTO.ResponseColosseum board = boardService.getColosseumBoard(boardUUID);
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


    @GetMapping("/myPage/{myPageUUID}")
    public ResponseEntity<APIDataResponse<RedisMyPage>> getMyPage(@PathVariable UUID myPageUUID) throws Exception{
        RedisMyPage testDto = matchingService.getBoardInRedis(myPageUUID);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(testDto)
                );
    }
    @PostMapping("/myPage")
    public ResponseEntity<APIDataResponse<RedisMyPage>> getMyPage(@RequestBody MyPageDTO myPageDTO) throws Exception{}
}
