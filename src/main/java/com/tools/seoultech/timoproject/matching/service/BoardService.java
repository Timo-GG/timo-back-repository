package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.DuoBoard;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.ScrimBoard;
import com.tools.seoultech.timoproject.matching.domain.board.repository.DuoBoardRepository;
import com.tools.seoultech.timoproject.matching.domain.board.repository.ScrimBoardRepository;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.BoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.service.mapper.BoardMapper;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final MemberService memberService;
    private final RiotAPIService bas;
    private final RedisKeyValueTemplate redisKeyValueTemplate;
    private final RedisTemplate redisTemplate;

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

    public boolean existsDuoByMemberId(Long memberId) {
        return duoBoardRepository.findByMemberId(memberId)
                .filter(board -> board.getBoardUUID() != null && board.getUpdatedAt() != null)
                .isPresent();
    }

    public boolean existsScrimByMemberId(Long memberId) {
        return scrimBoardRepository.findByMemberId(memberId)
                .filter(board -> board.getBoardUUID() != null && board.getUpdatedAt() != null)
                .isPresent();
    }

    /** 게시글 수정 업데이트 */
    public DuoBoard updateDuoBoard(BoardDTO.RequestUpdateDuo dto) throws Exception {
        DuoBoard oldEntity = duoBoardRepository.findById(dto.boardUUID())
                .orElseThrow(() -> new Exception("Board Not Found " + dto.boardUUID()));

        // 기존 TTL 조회
        String redisKey = "DuoBoard:" + dto.boardUUID();
        Long remainingTtl = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);

        // 기존 updatedAt 보존하여 업데이트
        DuoBoard updatedEntity = boardMapper.toUpdatedEntity(oldEntity, dto);
        updatedEntity = updatedEntity.toBuilder()
                .updatedAt(oldEntity.getUpdatedAt())
                .build();

        redisKeyValueTemplate.update(updatedEntity);

        // TTL이 있었다면 다시 설정
        if (remainingTtl != null && remainingTtl > 0) {
            redisTemplate.expire(redisKey, remainingTtl, TimeUnit.SECONDS);
        }

        return updatedEntity;
    }

    public ScrimBoard updateScrimBoard(BoardDTO.RequestUpdateScrim dto) throws Exception {
        ScrimBoard oldEntity = scrimBoardRepository.findById(dto.boardUUID())
                .orElseThrow(() -> new Exception("Board Not Found " + dto.boardUUID()));

        // 기존 TTL 조회
        String redisKey = "ScrimBoard:" + dto.boardUUID();
        Long remainingTtl = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);

        // 기존 updatedAt 보존하여 업데이트
        ScrimBoard updatedEntity = boardMapper.toUpdatedEntity(oldEntity, dto);
        updatedEntity = updatedEntity.toBuilder()
                .updatedAt(oldEntity.getUpdatedAt())
                .build();

        redisKeyValueTemplate.update(updatedEntity);

        // TTL이 있었다면 다시 설정
        if (remainingTtl != null && remainingTtl > 0) {
            redisTemplate.expire(redisKey, remainingTtl, TimeUnit.SECONDS);
        }

        return updatedEntity;
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

    /** 페이징된 듀오 게시글 조회 (최신순) */
    public BoardDTO.PageResponse getAllDuoBoardsWithPaging(int page, int size) {
        List<DuoBoardOnly> allBoards = duoBoardRepository.findAllBy()
                .stream()
                .filter(Objects::nonNull) // TTL 만료로 null된 것들 제거
                .filter(board -> {
                    try {
                        return board.getBoardUUID() != null &&
                                board.getUpdatedAt() != null;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .sorted((b1, b2) -> b2.getUpdatedAt().compareTo(b1.getUpdatedAt())) // 최신순 정렬
                .collect(Collectors.toList());

        // 페이징 처리
        int start = page * size;
        int end = Math.min(start + size, allBoards.size());

        if (start >= allBoards.size()) {
            // 페이지가 범위를 벗어난 경우 빈 결과 반환
            return BoardDTO.PageResponse.builder()
                    .content(List.of())
                    .page(page)
                    .size(size)
                    .totalElements(allBoards.size())
                    .totalPages((int) Math.ceil((double) allBoards.size() / size))
                    .first(page == 0)
                    .last(true)
                    .hasNext(false)
                    .hasPrevious(page > 0)
                    .build();
        }

        List<DuoBoardOnly> pagedBoards = allBoards.subList(start, end);
        List<BoardDTO.Response> content = pagedBoards.stream()
                .map(proj -> (BoardDTO.Response) boardMapper.toDuoDto(proj))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) allBoards.size() / size);

        return BoardDTO.PageResponse.builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(allBoards.size())
                .totalPages(totalPages)
                .first(page == 0)
                .last(page >= totalPages - 1)
                .hasNext(page < totalPages - 1)
                .hasPrevious(page > 0)
                .build();
    }

    /** 페이징된 스크림 게시글 조회 (최신순) */
    public BoardDTO.PageResponse getAllScrimBoardsWithPaging(int page, int size) {
        List<ScrimBoardOnly> allBoards = scrimBoardRepository.findAllBy()
                .stream()
                .filter(Objects::nonNull)
                .filter(board -> {
                    try {
                        return board.getBoardUUID() != null &&
                                board.getUpdatedAt() != null;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .sorted((b1, b2) -> b2.getUpdatedAt().compareTo(b1.getUpdatedAt())) // 최신순 정렬
                .collect(Collectors.toList());

        // 페이징 처리
        int start = page * size;
        int end = Math.min(start + size, allBoards.size());

        if (start >= allBoards.size()) {
            return BoardDTO.PageResponse.builder()
                    .content(List.of())
                    .page(page)
                    .size(size)
                    .totalElements(allBoards.size())
                    .totalPages((int) Math.ceil((double) allBoards.size() / size))
                    .first(page == 0)
                    .last(true)
                    .hasNext(false)
                    .hasPrevious(page > 0)
                    .build();
        }

        List<ScrimBoardOnly> pagedBoards = allBoards.subList(start, end);
        List<BoardDTO.Response> content = pagedBoards.stream()
                .map(proj -> (BoardDTO.Response) boardMapper.toScrimDto(proj))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) allBoards.size() / size);

        return BoardDTO.PageResponse.builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(allBoards.size())
                .totalPages(totalPages)
                .first(page == 0)
                .last(page >= totalPages - 1)
                .hasNext(page < totalPages - 1)
                .hasPrevious(page > 0)
                .build();
    }

    /** 페이징된 우리 학교 한정 스크림 게시글 조회 (최신순) */
    public BoardDTO.PageResponse getAllUnivScrimBoardsWithPaging(int page, int size, String univName) {
        List<ScrimBoardOnly> allBoards = scrimBoardRepository.findAllByUnivName(univName)
                .stream()
                .filter(Objects::nonNull)
                .filter(board -> {
                    try {
                        return board.getBoardUUID() != null &&
                                board.getUpdatedAt() != null;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .sorted((b1, b2) -> b2.getUpdatedAt().compareTo(b1.getUpdatedAt())) // 최신순 정렬
                .collect(Collectors.toList());

        // 페이징 처리
        int start = page * size;
        int end = Math.min(start + size, allBoards.size());

        if (start >= allBoards.size()) {
            return BoardDTO.PageResponse.builder()
                    .content(List.of())
                    .page(page)
                    .size(size)
                    .totalElements(allBoards.size())
                    .totalPages((int) Math.ceil((double) allBoards.size() / size))
                    .first(page == 0)
                    .last(true)
                    .hasNext(false)
                    .hasPrevious(page > 0)
                    .build();
        }

        List<ScrimBoardOnly> pagedBoards = allBoards.subList(start, end);
        List<BoardDTO.Response> content = pagedBoards.stream()
                .map(proj -> (BoardDTO.Response) boardMapper.toScrimDto(proj))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) allBoards.size() / size);

        return BoardDTO.PageResponse.builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(allBoards.size())
                .totalPages(totalPages)
                .first(page == 0)
                .last(page >= totalPages - 1)
                .hasNext(page < totalPages - 1)
                .hasPrevious(page > 0)
                .build();
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

    public void deleteByMemberId(Long memberId, MatchingCategory category) {
        switch (category) {
            case DUO:
                // findByMemberId로 찾아서 delete 사용 (인덱스 자동 정리)
                duoBoardRepository.findByMemberId(memberId)
                        .ifPresent(duoBoardRepository::delete);
                break;
            case SCRIM:
                scrimBoardRepository.findByMemberId(memberId)
                        .ifPresent(scrimBoardRepository::delete);
                break;
        }
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

    public DuoBoard refreshMyDuoBoard(Long memberId) {
        DuoBoard existingBoard = duoBoardRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("No DuoBoard found for memberId: " + memberId));

        DuoBoard refreshedBoard = existingBoard.toBuilder()
                .updatedAt(LocalDateTime.now())
                .build();

        return duoBoardRepository.save(refreshedBoard);
    }

    public ScrimBoard refreshMyScrimBoard(Long memberId) {
        ScrimBoard existingBoard = scrimBoardRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("No ScrimBoard found for memberId : " + memberId));

        ScrimBoard refreshedBoard = existingBoard.toBuilder()
                .updatedAt(LocalDateTime.now())
                .build();

        return scrimBoardRepository.save(refreshedBoard);
    }
}
