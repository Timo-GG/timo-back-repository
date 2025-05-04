package com.tools.seoultech.timoproject.auth.univ;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record UnivRequestDTO(
        String univName,
        @Email String univEmail
) {
}
