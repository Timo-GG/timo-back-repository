package com.tools.seoultech.timoproject.auth.univ;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
@Schema(description = "대학교 인증 요청 DTO")
public record UnivRequestDTO(
        @Schema(description = "대학교 이름", example = "서울과학기술대학교")
        String univName,

        @Schema(description = "대학교 이메일 주소", example = "menten4859@seoultech.ac.kr")
        @Email
        String univEmail
) {
}