package com.tools.seoultech.timoproject.matching.domain.myPage.dto;


import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;


public class MatchingDTO {
    // NOTE: 시스템 매칭 로직 DTO.
    public record RequestDuo(
            UUID boardUUID,
            Long requestorId,
            UserInfo userInfo,
            DuoInfo duoInfo
    ){}

    public record RequestScrim(
            UUID boardUUID,
            Long requestorId,
            List<RiotAccount> partyInfo
    ){}

    @Builder
    public record ResponseDuo(
            UUID myPageUUID,
            WrappedDuoData acceptor,
            WrappedDuoData requestor
    ){}

    @Builder
    public record ResponseScrim(
            UUID myPageUUID,
            WrappedScrimData acceptor,
            WrappedScrimData requestor
    ){}

    @Builder
    public record WrappedDuoData(
    // TODO: 유저의 데이터 정보도 넘겨야하나? memberAccount나 RiotAccount나 memberId. 가지고 있는 데이터인지 모르겠음.
            UserInfo userInfo,
            DuoInfo duoInfo,
            CompactPlayerHistory compactPlayerHistory
    ){}

    @Builder
    public record  WrappedScrimData(
            List<RiotAccount> partyInfo
    ){}

}
