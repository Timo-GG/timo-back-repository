package com.tools.seoultech.timoproject.matching.board.dto;

import com.tools.seoultech.timoproject.matching.myPage.entity.EnumType.MatchingCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SearchBoardDTO {
    private final MatchingCategory matchingCategory;
    private final Object dto;

    @Builder
    public record RequestDuoBoard(){}

    @Builder
    public record RequestColosseumBoard(){}

    @Builder
    public record ResponseDuoBoard(){}

    @Builder
    public record ResponseColosseumBoard(){}

    public SearchBoardDTO of(MatchingCategory matchingCategory, RequestDuoBoard dto) {
        return new SearchBoardDTO(matchingCategory, dto);
    }
    public SearchBoardDTO of(MatchingCategory matchingCategory, ResponseDuoBoard dto) {
        return new SearchBoardDTO(matchingCategory, dto);
    }
    public SearchBoardDTO of(MatchingCategory matchingCategory, RequestColosseumBoard dto) {
        return new SearchBoardDTO(matchingCategory, dto);
    }
    public SearchBoardDTO of(MatchingCategory matchingCategory, ResponseColosseumBoard dto) {
        return new SearchBoardDTO(matchingCategory, dto);
    }
}
