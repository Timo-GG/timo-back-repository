package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.service.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final UserService userService;
    private final RedisBoardRepository redisBoardRepository;
    private final BoardMapper boardMapper;

    /**
     * Duo 게시판에 게시글을 저장
     */
    public RedisBoard.Duo saveDuoBoard(BoardDTO.RequestDuo requestDuo) {
        // 사용자 정보 저장 및 UUID 추출
        RedisUser.Duo savedUser = userService.saveDuoUser(requestDuo.requestUserDto());

        // 게시글 DTO → RedisBoard 엔티티 변환
        RedisBoard.Duo redisBoard = boardMapper.toRedisDuo(requestDuo, savedUser);
        return redisBoardRepository.save(redisBoard);
    }

    /**
     * Colosseum 게시판에 게시글을 저장
     */
    public RedisBoard.Colosseum saveColosseumBoard(BoardDTO.RequestScrim requestScrim) {
        // 사용자 정보 저장 및 UUID 추출
        RedisUser.Colosseum savedUser = userService.saveColosseumUser(requestScrim.requestUserDto());

        // 게시글 DTO → RedisBoard 엔티티 변환
        RedisBoard.Colosseum redisBoard = boardMapper.toRedisColosseum(requestScrim, savedUser);
        return redisBoardRepository.save(redisBoard);
    }

    /**
     * Redis에서 단일 Duo 게시판 게시글 조회
     */
    public BoardDTO.ResponseDuo getDuoBoard(UUID boardUUID) throws Exception {
        RedisBoard.Duo redisBoard = (RedisBoard.Duo) redisBoardRepository
                .findById(boardUUID)
                .orElseThrow(() -> new Exception("Board Not Found : " + boardUUID));
        return boardMapper.toResponseDuo(redisBoard);
    }

    /**
     * Redis에서 Colosseum 게시판 게시글 조회
     */
    public BoardDTO.ResponseScrim getScrimBoard(UUID boardUUID) throws Exception {
        RedisBoard.Colosseum redisBoard = (RedisBoard.Colosseum) redisBoardRepository
                .findById(boardUUID)
                .orElseThrow(() -> new Exception("Board not found: " + boardUUID));
        return boardMapper.toResponseColosseum(redisBoard);
    }

    /**
     * 모든 Duo 게시글 조회
     */
    public List<BoardDTO.ResponseDuo> getAllDuoBoards() {
        return redisBoardRepository
                .findAllByMatchingCategory(MatchingCategory.DUO)
                .stream()
                .map(b -> boardMapper.toResponseDuo((RedisBoard.Duo) b))
                .toList();
    }

    /**
     * 모든 Colosseum 게시글 조회
     */
    public List<BoardDTO.ResponseScrim> getAllColosseumBoards() {
        return redisBoardRepository
                .findAllByMatchingCategory(MatchingCategory.SCRIM)
                .stream()
                .map(b -> boardMapper.toResponseColosseum((RedisBoard.Colosseum) b))
                .toList();
    }

    /**
     * UUID로 게시글 삭제
     */
    public void deleteBoardByUUID(UUID boardUUID) throws Exception {
        RedisBoard redisBoard = redisBoardRepository
                .findById(boardUUID)
                .orElseThrow(() -> new Exception("Board not found: " + boardUUID));
        redisBoardRepository.delete(redisBoard);
    }

    /**
     * 모든 게시글 삭제
     */
    public void deleteAllBoards() {
        redisBoardRepository.deleteAll();
    }

    /**
     * 유형별(Duo) 전체 삭제
     */
    public void deleteAllDuoBoards() {
        redisBoardRepository.deleteAll(redisBoardRepository
                .findAllByMatchingCategory(MatchingCategory.DUO));
    }

    /**
     * 유형별(Colosseum) 전체 삭제
     */
    public void deleteAllColosseumBoards() {
        redisBoardRepository.deleteAll(redisBoardRepository
                .findAllByMatchingCategory(MatchingCategory.SCRIM));
    }
}
