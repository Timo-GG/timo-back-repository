package com.tools.seoultech.timoproject.matching.service.facade;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;

import java.util.List;
import java.util.UUID;

public interface BoardFacade {
        BoardDTO.Response create(BoardDTO.Request dto);

        BoardDTO.Response read(UUID boardUUID) throws Exception;
        BoardDTO.PageResponse readAllWithPaging(MatchingCategory matchingCategory, int page, int size);

        BoardDTO.Response update(BoardDTO.Request dto) throws Exception;
        void delete(UUID boardUUID);
        void deleteAll(MatchingCategory matchingCategory);

        boolean existsByMemberId(Long memberId);
        BoardDTO.Response refreshMyDuoBoard(Long memberId);

        void deleteByMemberId(Long memberId, MatchingCategory category) throws Exception;}
