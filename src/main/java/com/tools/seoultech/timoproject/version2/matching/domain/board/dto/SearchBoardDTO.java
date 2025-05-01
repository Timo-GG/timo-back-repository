package com.tools.seoultech.timoproject.version2.matching.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tools.seoultech.timoproject.version2.matching.domain.board.entity.enumType.ColosseumModeCode;
import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.version2.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.CompactPlayerHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class SearchBoardDTO <T extends SearchBoardDTO.SearchBoardDTOInterface> {
    // 공통 필드
    private final String memo;
    private final T dto;

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
    public SearchBoardDTO(String memo, T dto){
        this.memo = memo;
        this.dto = dto;
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
