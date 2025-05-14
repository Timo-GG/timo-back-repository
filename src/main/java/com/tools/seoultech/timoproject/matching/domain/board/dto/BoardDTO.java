package com.tools.seoultech.timoproject.matching.domain.board.dto;

import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;


@Getter
public abstract class BoardDTO {
    // TODO: CompactRiotHistory 필드 추가.

    @Builder
    public record RequestSearch(
            Long memberId,
            MatchingCategory matchingCategory,
            PlayPosition position
    ) implements Request {}

    @Builder
    public record RequestDuo(
        // NOTE: UserDTO 필드 : RedisUser
            Long memberId,
            UserInfo userInfo,
            DuoInfo duoInfo,
        // NOTE: 나머지 DuoBoard 필드
            String memo,
            DuoMapCode mapCode
    ) implements Request {}

    @Builder
    public record RequestColosseum(
        // NOTE: UserDTO 필드 : RedisUser
            Long memberId,
            List<RiotAccount> partyMemberRiotAccountList,
        // NOTE: 나머지 ColosseumBoard 필드
            String memo,
            ColosseumMapCode mapCode,
            Integer headCount
    )implements Request{}

    @Builder
    public record ResponseDuo(
            UUID boardUUID,
            String memo,
            DuoMapCode mapCode,
            UserDTO.ResponseDuo responseUserDto
    ) implements Response {}

    @Builder
    public record ResponseColosseum(
            UUID boardUUID,
            String memo,
            ColosseumMapCode mapCode,
            Integer headCount,
            UserDTO.ResponseColosseum responseUserDto
    ) implements Response {}

    // 인터페이스.
    public interface Request extends BaseInterface {}
    public interface Response extends BaseInterface {}
    public interface BaseInterface {}
}