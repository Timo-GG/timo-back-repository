package com.tools.seoultech.timoproject.matching.service.facade.Impl;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.DuoBoard;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.ScrimBoard;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.BoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.service.BoardService;
import com.tools.seoultech.timoproject.matching.service.facade.BoardFacade;
import com.tools.seoultech.timoproject.matching.service.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BoardFacadeImpl implements BoardFacade {
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @Override
    public BoardDTO.Response create(BoardDTO.Request dto) {
        if(dto instanceof BoardDTO.RequestDuo duoDto){
            var saved = boardService.saveDuoBoard(duoDto);
            return boardMapper.toDuoDto(saved);
        }
        else if (dto instanceof  BoardDTO.RequestScrim scrimDto) {
            var saved = boardService.saveScrimBoard(scrimDto);
            return boardMapper.toScrimDto(saved);
        }
        throw new GeneralException("[Invalid Data] 매칭되는 DTO 타입이 없습니다.");
    }

    @Override
    public BoardDTO.Response read(UUID boardUUID) throws Exception {
        BoardOnly proj = boardService.getBoard(boardUUID);
        if(proj instanceof DuoBoardOnly){
            return boardMapper.toDuoDto((DuoBoardOnly) proj);
        } else {
            return boardMapper.toScrimDto((ScrimBoardOnly) proj);
        }
    }

    @Override
    public BoardDTO.PageResponse readAllWithPaging(MatchingCategory matchingCategory, int page, int size) {
        if(matchingCategory == MatchingCategory.DUO){
            return boardService.getAllDuoBoardsWithPaging(page, size);
        } else {
            return boardService.getAllScrimBoardsWithPaging(page, size);
        }
    }


    @Override
    public BoardDTO.Response update(BoardDTO.Request dto) throws Exception{
        if(dto instanceof BoardDTO.RequestUpdateDuo duoDto){
            DuoBoard newEntity = boardService.updateDuoBoard(duoDto);
            return boardMapper.toDuoDto(newEntity);
        }
        else if(dto instanceof  BoardDTO.RequestUpdateScrim scrimDto) {
            ScrimBoard newEntity = boardService.updateScrimBoard(scrimDto);
            return boardMapper.toScrimDto(newEntity);
        }
        else throw new GeneralException("해당 Board update 실패.");
    }

    @Override
    public void delete(UUID boardUUID) {
        boardService.deleteDuoBoardById(boardUUID);
        boardService.deleteScrimBoardById(boardUUID);
    }

    @Override
    public void deleteAll(MatchingCategory matchingCategory) {
        if(matchingCategory == MatchingCategory.DUO){
            boardService.deleteAllDuoBoards();
        } else {
            boardService.deleteAllScrimBoards();
        }
    }

    @Override
    public boolean existsDuoByMemberId(Long memberId) {
        return boardService.existsDuoByMemberId(memberId);
    }

    @Override
    public boolean existsScrimByMemberId(Long memberId) {
        return boardService.existsScrimByMemberId(memberId);
    }

    @Override
    public BoardDTO.Response refreshMyDuoBoard(Long memberId) {
        return boardMapper.toDuoDto(boardService.refreshMyDuoBoard(memberId));
    }

    @Override
    public BoardDTO.Response refreshMyScrimBoard(Long memberId) {
        return boardMapper.toScrimDto(boardService.refreshMyScrimBoard(memberId));
    }

    @Override
    public void deleteByMemberId(Long memberId, MatchingCategory category) throws Exception {
        boardService.deleteByMemberId(memberId, category);
    }
}
