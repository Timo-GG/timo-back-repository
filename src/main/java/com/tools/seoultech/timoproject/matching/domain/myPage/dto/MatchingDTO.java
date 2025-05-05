package com.tools.seoultech.timoproject.matching.domain.myPage.dto;

import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class MatchingDTO {
    // NOTE: 듀오, 내전 보드판 보고서 신청했을 때의 매칭 DTO.
    @Builder
    public record RequestDuo(
            UUID boardUUID,
            UserDTO.RequestDuo requestorDto
    ){}
    public record RequestColosseum(
            UUID boardUUID,
            UserDTO.RequestColosseum requestorDto
    ){}
}
