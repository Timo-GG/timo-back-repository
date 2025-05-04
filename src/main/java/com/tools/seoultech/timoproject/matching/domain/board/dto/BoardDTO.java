package com.tools.seoultech.timoproject.matching.domain.board.dto;

import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumModeCode;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

//@Schema(
//        description = "Duo 또는 Colosseum 게시글 공통 DTO",
//        discriminatorProperty = "type",
//        discriminatorMapping = {
//                @DiscriminatorMapping(value = "duo", schema = BoardDTO.RequestDuo.class),
//                @DiscriminatorMapping(value = "colosseum", schema = BoardDTO.RequestColosseum.class)
//        }
//)
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = BoardDTO.RequestDuo.class, name = "duo"),
//        @JsonSubTypes.Type(value = BoardDTO.RequestColosseum.class, name = "colosseum")
//})
@Getter
public class BoardDTO {
    // TODO: CompactRiotHistory 필드 추가.
    @Builder
    public record RequestDuo(
            UserDTO.RequestDuo RequestUserDto,
            String memo
    ) implements Request {}

    @Builder
    public record RequestColosseum(
        UserDTO.RequestDuo requestUserDto,
        String memo,
        ColosseumMapCode mapCode,
        Integer headCount
    )implements Request{}

    @Builder
    public record ResponseDuo(
            UUID boardUUID,
            UserDTO.ResponseDuo responseUserDto,
            String memo
    ) implements Response {}

    @Builder
    public record ResponseColosseum(
            UUID boardUUID,
            UserDTO.ResponseColosseum responseUserDto,
            String memo,
            ColosseumMapCode mapCode,
            Integer headCount
    ) implements Response {}

    // 인터페이스.
    public interface Request extends BaseInterface {}
    public interface Response extends BaseInterface {}
    public interface BaseInterface {}
}
