package com.tools.seoultech.timoproject.matching.domain.myPage.dto;

import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class MatchingDTO {
    // NOTE: 시스템 매칭관련 DTO.
    //  - 유저 조회용 X.
    @Builder
    public record RequestDuo(
            String boardUUID,
            UserDTO.RequestDuo duoRequestorDto
    ){}
    public record RequestColosseum(
            String boardUUID,
            UserDTO.RequestColosseum colosseumRequestorDto
    ){}
}
