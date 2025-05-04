package com.tools.seoultech.timoproject.matching.controller;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoardDTO;
import com.tools.seoultech.timoproject.matching.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/duo")
@RequiredArgsConstructor
@Tag(name = "Duo", description = "Duo API")
public class DuoController {

    private final BoardService boardService;

    /**
     * 1. 유저 정보를 Redis에 저장 (RequestDto → RedisUserDTO)
     * 2. 내부에서 UserService + UserMapper 동작해서 정리
     * 3. 게시글 정보를 Redis에 저장 : BoardDTO<RequestDuo> + RedisUserDTO → RedisBoardDTO
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RedisBoardDTO<?>> registerDuo(
            @RequestBody @Valid BoardDTO<BoardDTO.RequestDuo> boardDto
    ) {
        RedisBoardDTO<?> saved = boardService.setBoardInRedis(boardDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    /**
     * 등록된 듀오 게시글 조회 API
     * Redis에 저장된 게시글을 uuid 기반으로 조회
     *
     * @param uuid : Redis에 저장된 게시글의 고유 ID
     * @return : RedisBoardDTO<BoardDTO.ResponseDuo> 게시글 반환
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<RedisBoardDTO<?>> getDuo(
            @PathVariable("uuid") String uuid
    ) {
        RedisBoardDTO<?> duo = boardService.getDuoBoardFromRedis(uuid);
        return ResponseEntity.ok(duo);
    }

    /** 등록된 듀오 게시글 전체 목록 조회 */
    @GetMapping
    public ResponseEntity<List<RedisBoardDTO<BoardDTO.ResponseDuo>>> listDuo() {
        List<RedisBoardDTO<BoardDTO.ResponseDuo>> allDuos = boardService.listAllDuoBoards();
        return ResponseEntity.ok(allDuos);
    }
}
