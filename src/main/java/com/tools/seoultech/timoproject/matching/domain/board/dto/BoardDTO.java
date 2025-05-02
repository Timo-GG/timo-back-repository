package com.tools.seoultech.timoproject.matching.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumModeCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.CompactPlayerHistory;
import lombok.Builder;
import lombok.Getter;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type") // NOTE: 직렬화 시 타입정보 저장
@JsonSubTypes({  // NOTE: 역직렬화 시 허용된 클래스만 역직렬화
        @JsonSubTypes.Type(value = BoardDTO.ResponseDuo.class, name = "duo"),
        @JsonSubTypes.Type(value = BoardDTO.ResponseColosseum.class, name = "colosseum")
})
@Getter
public class BoardDTO<T extends BoardDTO.BaseInterface> {
    // 공통 필드
    private final String memo;
    private final T body;

    // 중첩 Record 필드. 1. Request•Response, 2. Duo•Colosseum
    @Builder
    public record RequestDuo(UserDTO<UserDTO.RequestDuo> requestUserDto) implements Request {
        @Override
        public UserDTO<? extends UserDTO.Request> getUserDtoInRequestBody() {
            return this.requestUserDto;
        }
    }

    @Builder
    public record RequestColosseum(
            ColosseumModeCode mapCode,
            UserDTO<UserDTO.RequestColosseum> requestUserDto) implements Request {
        @Override
        public UserDTO<? extends UserDTO.Request> getUserDtoInRequestBody() {
            return this.requestUserDto;
        }
    }

    @Builder
    public record ResponseDuo (
            UserDTO<UserDTO.ResponseDuo> responseUserDto,
            CompactPlayerHistory compactPlayerHistory
    ) implements Response {}

    @Builder
    public record ResponseColosseum(
            ColosseumModeCode mapCode,
            UserDTO<UserDTO.ResponseColosseum> responseUserDto
    ) implements Response { }

    // 생성자 Builder
    @Builder
    public BoardDTO(String memo, T body){
        this.memo = memo;
        this.body = body;
    }

    // 인터페이스.
    public interface Request extends BaseInterface {
        UserDTO<? extends UserDTO.Request> getUserDtoInRequestBody();
    }
    public interface Response extends BaseInterface { }
    public interface BaseInterface {
        default MatchingCategory getMatchingCategory() {
            if (this instanceof BoardDTO.RequestDuo || this instanceof BoardDTO.ResponseDuo) {
                return MatchingCategory.Duo;
            } else {
                return MatchingCategory.Colosseum;
            }
        }
    }
}
