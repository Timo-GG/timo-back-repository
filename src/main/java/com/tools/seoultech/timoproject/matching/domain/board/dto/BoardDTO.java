package com.tools.seoultech.timoproject.matching.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumModeCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.CompactPlayerHistory;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(
        description = "Duo 또는 Colosseum 게시글 공통 DTO",
        discriminatorProperty = "type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = "duo", schema = BoardDTO.RequestDuo.class),
                @DiscriminatorMapping(value = "colosseum", schema = BoardDTO.RequestColosseum.class)
        }
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BoardDTO.RequestDuo.class, name = "duo"),
        @JsonSubTypes.Type(value = BoardDTO.RequestColosseum.class, name = "colosseum")
})
public record BoardDTO<T extends BoardDTO.BaseInterface>(String memo, T body) {

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
    public record ResponseDuo(
            UserDTO<UserDTO.ResponseDuo> responseUserDto,
            CompactPlayerHistory compactPlayerHistory
    ) implements Response {
    }

    @Builder
    public record ResponseColosseum(
            ColosseumModeCode mapCode,
            UserDTO<UserDTO.ResponseColosseum> responseUserDto
    ) implements Response {
    }

    // 생성자 Builder
    @Builder
    public BoardDTO {
    }

    // 인터페이스.
    public interface Request extends BaseInterface {
        UserDTO<? extends UserDTO.Request> getUserDtoInRequestBody();
    }

    public interface Response extends BaseInterface {
    }

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
