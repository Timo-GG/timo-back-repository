package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.DuoBoard;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.ScrimBoard;
import com.tools.seoultech.timoproject.matching.domain.board.repository.DuoBoardRepository;
import com.tools.seoultech.timoproject.matching.domain.board.repository.ScrimBoardRepository;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.service.mapper.BoardMapper;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardService boardService;
    private final MemberService memberService;
    private final BasicAPIService bas;

    private final DuoBoardRepository duoBoardRepository;
    private final ScrimBoardRepository scrimBoardRepository;

    private final BoardMapper boardMapper;

    /** Duo 게시판에 게시글을 저장*/
    public BoardDTO.ResponseDuo saveDuoBoard(BoardDTO.RequestDuo dto) {
        // 게시글 DTO → RedisBoard 엔티티 변환
        DuoBoard saved = duoBoardRepository.save(boardMapper.toDuoRedis(dto, memberService, bas));
        return boardMapper.toDuoDto(saved);
    }

    /** Colosseum 게시판에 게시글을 저장 */
    public BoardDTO.ResponseScrim saveColosseumBoard(BoardDTO.RequestScrim requestScrim) {
        // 게시글 DTO → RedisBoard 엔티티 변환
        ScrimBoard saved = scrimBoardRepository.save(boardMapper.toScrimRedis(requestScrim, memberService, bas));
        return boardMapper.toScrimDto(saved);
    }

    /** Redis에서 단일 Duo 게시판 게시글 조회 */
    public DuoBoardOnly getDuoBoard(UUID boardUUID) throws Exception {
        DuoBoardOnly proj = duoBoardRepository.findByBoardUUID(boardUUID)
                .orElseThrow(() -> new Exception("Board Not Found : " + boardUUID));
        return proj;
    }

    /** Redis에서 Colosseum 게시판 게시글 조회 */
    public ScrimBoardOnly getScrimBoard(UUID boardUUID) throws Exception {
        ScrimBoardOnly proj = scrimBoardRepository.findByBoardUUID(boardUUID)
                .orElseThrow(() -> new Exception("Board not found: " + boardUUID));
        return proj;
    }

    /** 모든 게시글 조회 */
    public List<DuoBoardOnly> getAllDuoBoards() {
        return duoBoardRepository.findAllBy();
    }

    public List<ScrimBoardOnly> getAllColosseumBoards() {
        return scrimBoardRepository.findAllBy();
    }

    /** UUID 게시글 삭제 */
    public void deleteDuoBoardById(UUID boardUUID) throws Exception {
        duoBoardRepository.deleteById(boardUUID);
    }

    public void deleteScrimBoardById(UUID boardUUID) throws Exception {
        scrimBoardRepository.deleteById(boardUUID);
    }

    /** 전체 삭제 */
    public void deleteAllDuoBoards() {
        duoBoardRepository.deleteAll();
    }

    public void deleteAllColosseumBoards() {
        scrimBoardRepository.deleteAll();
    }
}
