package com.tools.seoultech.timoproject.version2.matching.domain.board.dto;

import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.MatchingCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash("BoardDTO")
@RequiredArgsConstructor
@Getter
public class SearchBoardDTO <T extends SearchBoardDTO.SearchBoardDTOInterface> {
    public interface SearchBoardDTOInterface{}

    private final Long id;
    private final MatchingCategory matchingCategory;
    private final Boolean isRequest;
    private final String memo;
    private final T dto;

    private final LocalDateTime createdAt;
    @Builder
    public record RequestDuoBoard(

    ) implements SearchBoardDTOInterface {}

    @Builder
    public record RequestColosseumBoard(

    ) implements SearchBoardDTOInterface{}

    @Builder
    public record ResponseDuoBoard(

    ) implements SearchBoardDTOInterface{}

    @Builder
    public record ResponseColosseumBoard(

    ) implements SearchBoardDTOInterface{}
}
