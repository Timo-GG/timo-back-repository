package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoardRepository;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUserRepository;
import com.tools.seoultech.timoproject.matching.service.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final RedisBoardRepository redisBoardRepository;
    private final RedisUserRepository redisUserRepository;
    private final BoardMapper boardMapper;

    /**
     * Duo 게시판에 게시글을 저장
     */
    public RedisBoard.Duo saveDuoBoard(BoardDTO.RequestDuo requestDuo, UUID userUUID) {
        RedisBoard.Duo redisBoard = boardMapper.toRedisDuo(requestDuo, userUUID);
        return redisBoardRepository.save(redisBoard);
    }

    /**
     * Colosseum 게시판에 게시글을 저장
     */
    public RedisBoard.Colosseum saveColosseumBoard(BoardDTO.RequestColosseum requestColosseum, UUID userUUID) {
        RedisBoard.Colosseum redisBoard = boardMapper.toRedisColosseum(requestColosseum, userUUID);
        return redisBoardRepository.save(redisBoard);
    }

    /**
     * Redis에서 단일 Duo 게시판 게시글 조회
     */
    public BoardDTO.ResponseDuo getDuoBoard(UUID boardUUID) throws Exception {
        RedisBoard.Duo redisBoard = (RedisBoard.Duo) redisBoardRepository
                .findById(boardUUID)
                .orElseThrow(() -> new Exception("Board Not Found : " + boardUUID));
        return boardMapper.toResponseDuo(redisBoard, redisUserRepository);
    }

    /**
     * Redis에서 Colosseum 게시판 게시글 조회
     */
    public BoardDTO.ResponseColosseum getColosseumBoard(UUID boardUUID) throws Exception {
        RedisBoard.Colosseum redisBoard = (RedisBoard.Colosseum) redisBoardRepository
                .findById(boardUUID)
                .orElseThrow(() -> new Exception("Board not found: " + boardUUID));
        return boardMapper.toResponseColosseum(redisBoard, redisUserRepository);
    }

    /**
     * 모든 Duo 게시글 조회
     */
    public List<BoardDTO.ResponseDuo> getAllDuoBoards() {
        return redisBoardRepository
                .findAllByMatchingCategory(MatchingCategory.Duo)
                .stream()
                .map(b -> boardMapper.toResponseDuo((RedisBoard.Duo) b, redisUserRepository))
                .toList();
    }

    /**
     * 모든 Colosseum 게시글 조회
     */
    public List<BoardDTO.ResponseColosseum> getAllColosseumBoards() {
        return redisBoardRepository
                .findAllByMatchingCategory(MatchingCategory.Colosseum)
                .stream()
                .map(b -> boardMapper.toResponseColosseum((RedisBoard.Colosseum) b, redisUserRepository))
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
}
