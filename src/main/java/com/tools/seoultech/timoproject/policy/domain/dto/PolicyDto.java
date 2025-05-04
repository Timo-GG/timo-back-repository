package com.tools.seoultech.timoproject.policy.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PolicyDto(
        Long policyId,
        @NotNull Boolean usingAgreement,
        @NotNull Boolean collectingAgreement,
        LocalDateTime regDate,
        LocalDateTime modDate
) {
}
