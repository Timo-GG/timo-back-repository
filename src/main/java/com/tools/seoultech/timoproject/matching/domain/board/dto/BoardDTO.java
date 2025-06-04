package com.tools.seoultech.timoproject.matching.domain.board.dto;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.*;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Getter
public abstract class BoardDTO {
    public record RequestSearch(
            Long memberId,
            MatchingCategory matchingCategory,
            PlayPosition position
    ) implements Request {}

    public record RequestDuo(
            Long memberId,
            DuoMapCode mapCode,
            String memo,
            UserInfo userInfo,
            DuoInfo duoInfo
    ) implements Request {}

    public record RequestScrim(
            Long memberId,
            ScrimMapCode mapCode,
            String memo,
            Integer headCount,
            List<PartyMemberInfo> partyInfo
    )implements Request{}


    public record RequestUpdateDuo(
        UUID boardUUID,
        DuoMapCode mapCode,
        String memo,
        UserInfo userInfo,
        DuoInfo duoInfo
    ) implements Request {}

    public record RequestUpdateScrim(
        UUID boardUUID,
        ScrimMapCode mapCode,
        String memo,
        Integer headCount,
        List<PartyMemberInfo> partyInfo
    ) implements Request {}

    @Builder
    public record ResponseDuo(
            Long memberId,
            UUID boardUUID,
            CertifiedMemberInfo memberInfo,
            DuoMapCode mapCode,
            String memo,
            UserInfo userInfo,
            DuoInfo duoInfo,
            LocalDateTime updatedAt
    ) implements Response {}

    @Builder
    public record ResponseScrim(
            Long memberId,
            UUID boardUUID,
            CertifiedMemberInfo memberInfo,
            ScrimMapCode mapCode,
            String memo,
            Integer headCount,
            List<PartyMemberInfo> partyInfo,
            LocalDateTime updatedAt
    ) implements Response {}

    @Getter
    @Builder
    public static class PageResponse {
        private List<Response> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;
        private boolean hasNext;
        private boolean hasPrevious;
    }

    // 인터페이스.
    public interface Request extends BaseInterface {}
    public interface Response extends BaseInterface {}
    public interface BaseInterface {}
}