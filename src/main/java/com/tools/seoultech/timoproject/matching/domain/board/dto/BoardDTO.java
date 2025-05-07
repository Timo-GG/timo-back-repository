package com.tools.seoultech.timoproject.matching.domain.board.dto;

import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.enumType.PlayPosition;
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
@Getter
public abstract class BoardDTO {
    // TODO: CompactRiotHistory 필드 추가.

    @Builder
    public record RequestSearch(
            Long memberAccountId,
            MatchingCategory matchingCategory,
            PlayPosition position
    ) implements Request {}

    @Builder
    public record RequestDuo(
            UserDTO.RequestDuo requestUserDto,
            String memo,
            DuoMapCode duoMapCode
    ) implements Request {}

    @Builder
    public record RequestColosseum(
        UserDTO.RequestColosseum requestUserDto,
        String memo,
        ColosseumMapCode mapCode,
        Integer headCount
    )implements Request{}

    @Builder
    public record ResponseDuo(
            UUID boardUUID,
            UserDTO.ResponseDuo responseUserDto,
            String memo,
            DuoMapCode duoMapCode
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