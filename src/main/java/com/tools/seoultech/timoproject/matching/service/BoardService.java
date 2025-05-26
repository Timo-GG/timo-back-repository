package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.DuoBoard;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.ScrimBoard;
import com.tools.seoultech.timoproject.matching.domain.board.repository.DuoBoardRepository;
import com.tools.seoultech.timoproject.matching.domain.board.repository.ScrimBoardRepository;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.BoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import com.tools.seoultech.timoproject.matching.service.mapper.BoardMapper;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final MemberService memberService;
    private final RiotAPIService bas;

    private final DuoBoardRepository duoBoardRepository;
    private final ScrimBoardRepository scrimBoardRepository;

    private final BoardMapper boardMapper;

    /** 게시판에 게시글을 저장 */
    public DuoBoard saveDuoBoard(BoardDTO.RequestDuo dto) {
        DuoBoard saved = duoBoardRepository.save(boardMapper.toDuoRedis(dto, memberService, bas));
        return saved;
    }

    public ScrimBoard saveScrimBoard(BoardDTO.RequestScrim requestScrim) {
        ScrimBoard saved = scrimBoardRepository.save(boardMapper.toScrimRedis(requestScrim, memberService, bas));
        return saved;
    }


    /** 게시글 수정 업데이트 */
    public DuoBoard updateDuoBoard(BoardDTO.RequestUpdateDuo dto) throws Exception {
        DuoBoard oldEntity = duoBoardRepository.findById(dto.boardUUID())
                .orElseThrow(() -> new Exception("Board Not Found " + dto.boardUUID()));
        DuoBoard newEntity = duoBoardRepository.save(boardMapper.toUpdatedEntity(oldEntity, dto));
        return newEntity;
    }

    public ScrimBoard updateScrimBoard(BoardDTO.RequestUpdateScrim dto) throws Exception {
        ScrimBoard oldEntity = scrimBoardRepository.findById(dto.boardUUID())
                .orElseThrow(() -> new Exception("Board Not Found " + dto.boardUUID()));
        ScrimBoard newEntity = scrimBoardRepository.save(boardMapper.toUpdatedEntity(oldEntity, dto));
        return newEntity;
    }


    /** Redis에서 단일 Duo 게시판 게시글 조회 */
    public DuoBoardOnly getDuoBoard(UUID boardUUID) throws Exception {
        DuoBoardOnly proj = duoBoardRepository.findByBoardUUID(boardUUID)
                .orElseThrow(() -> new Exception("Board Not Found : " + boardUUID));
        return proj;
    }

    public ScrimBoardOnly getScrimBoard(UUID boardUUID) throws Exception {
        ScrimBoardOnly proj = scrimBoardRepository.findByBoardUUID(boardUUID)
                .orElseThrow(() -> new Exception("Board not found: " + boardUUID));
        return proj;
    }

    public BoardOnly getBoard(UUID boardUUID) throws Exception {
        BoardOnly proj = duoBoardRepository.findByBoardUUID(boardUUID)
                .map(p -> (BoardOnly) p)
                .or(() -> scrimBoardRepository.findByBoardUUID(boardUUID)
                    .map(p -> (BoardOnly) p))
                .orElseThrow(() -> new Exception("Board not found: " + boardUUID));
        return proj;
    }


    /** 모든 게시글 조회 */
    public List<DuoBoardOnly> getAllDuoBoards() {
        return duoBoardRepository.findAllBy()
                .stream()
                .filter(Objects::nonNull) // TTL 만료로 null된 것들 제거
                .collect(Collectors.toList());
    }

    public List<ScrimBoardOnly> getAllScrimBoards() {
        return scrimBoardRepository.findAllBy()
                .stream()
                .filter(Objects::nonNull) // TTL 만료로 null된 것들 제거
                .collect(Collectors.toList());
    }


    /** UUID 게시글 삭제 */
    public void deleteDuoBoardById(UUID boardUUID) {
        duoBoardRepository.deleteById(boardUUID);
    }

    public void deleteScrimBoardById(UUID boardUUID) {
        scrimBoardRepository.deleteById(boardUUID);
    }


    /** 전체 삭제 */
    public void deleteAllDuoBoards() {
        duoBoardRepository.deleteAll();
    }

    public void deleteAllScrimBoards() {
        scrimBoardRepository.deleteAll();
    }
}
