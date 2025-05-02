package com.tools.seoultech.timoproject.version2.matching.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tools.seoultech.timoproject.version2.matching.domain.board.entity.enumType.ColosseumModeCode;
import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.version2.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.CompactPlayerHistory;
import lombok.Builder;
import lombok.Getter;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type") // NOTE: 직렬화 시 타입정보 저장
@JsonSubTypes({  // NOTE: 역직렬화 시 허용된 클래스만 역직렬화
        @JsonSubTypes.Type(value = SearchBoardDTO.ResponseDuoBoard.class, name = "duo"),
        @JsonSubTypes.Type(value = SearchBoardDTO.ResponseColosseumBoard.class, name = "colosseum")
})
@Getter
public class SearchBoardDTO <T extends SearchBoardDTO.SearchBoardDTOInterface> {
    // 공통 필드
    private final String memo;
    private final T body;

    // 중첩 Record 필드. 1. Request•Response, 2. Duo•Colosseum
    @Builder
    public record RequestDuoBoard(
            UserDTO<UserDTO.RequestDuoUser> requestUserDto
    ) implements BoardRequestDTOInterface {}

    @Builder
    public record RequestColosseumBoard(
            ColosseumModeCode mapCode,
            UserDTO<UserDTO.RequestColosseumUser> requestUserDto
    ) implements BoardRequestDTOInterface {}

    @Builder
    public record ResponseDuoBoard(
            UserDTO<UserDTO.ResponseDuoUser> responseUserDto,
            CompactPlayerHistory compactPlayerHistory
    ) implements BoardResponseDTOInterface {}

    @Builder
    public record ResponseColosseumBoard(
            ColosseumModeCode mapCode,
            UserDTO<UserDTO.ResponseColoseumUser> responseUserDto
    ) implements BoardResponseDTOInterface {}

    // 생성자 Builder
    @Builder
    public SearchBoardDTO(String memo, T body){
        this.memo = memo;
        this.body = body;
    }

    // 인터페이스.
    public interface BoardResponseDTOInterface extends SearchBoardDTOInterface{}
    public interface BoardRequestDTOInterface extends SearchBoardDTOInterface{}
    public interface SearchBoardDTOInterface{
        default MatchingCategory getMatchingCategory() {
            if (this instanceof SearchBoardDTO.RequestDuoBoard || this instanceof SearchBoardDTO.ResponseDuoBoard) {
                return MatchingCategory.Duo;
            } else {
                return MatchingCategory.Colosseum;
            }
        }
    }
}
