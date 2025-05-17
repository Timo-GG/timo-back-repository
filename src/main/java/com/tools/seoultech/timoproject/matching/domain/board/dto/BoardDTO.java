package com.tools.seoultech.timoproject.matching.domain.board.dto;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.*;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
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
            Long memberId,
            DuoMapCode mapCode,
            String memo,
            UserInfo userInfo,
            DuoInfo duoInfo
    ) implements Request {}

    @Builder
    public record RequestScrim(
            Long memberId,
            ColosseumMapCode mapCode,
            String memo,
            Integer headCount,
            List<PartyMemberInfo> partyInfo
    )implements Request{}

    @Builder
    public record ResponseDuo(
            UUID boardUUID,
            CertifiedMemberInfo memberInfo,
            DuoMapCode mapCode,
            String memo,
            UserInfo userInfo,
            DuoInfo duoInfo
            ) implements Response {}

    @Builder
    public record ResponseScrim(
            UUID boardUUID,
            CertifiedMemberInfo memberInfo,
            ColosseumMapCode mapCode,
            String memo,
            Integer headCount,
            List<PartyMemberInfo> partyInfo
    ) implements Response {}

    // 인터페이스.
    public interface Request extends BaseInterface {}
    public interface Response extends BaseInterface {}
    public interface BaseInterface {}
}