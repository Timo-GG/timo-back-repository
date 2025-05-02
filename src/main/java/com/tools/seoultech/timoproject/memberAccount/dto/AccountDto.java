package com.tools.seoultech.timoproject.memberAccount.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class AccountDto {
    @Getter
    @Schema(description = "라이엇 계정 인증 요청 DTO")
    public static class Request{
        @Schema(description = "롤 닉네임", example = "짱아깨비")
        private final String gameName;
        @Schema(description = "태그라인", example = "k r    ")
        private final String tagLine;

        @JsonCreator
        public Request(@JsonProperty("gameName") String gameName,
                       @JsonProperty("tagLine") String tagLine) {
            this.gameName = gameName;
            this.tagLine = tagLine;
        }

        public static AccountDto.Request of(String gameName, String tagLine) {
            return new AccountDto.Request(gameName, tagLine);
        }
    }

    @Getter
    public static class Response {
        @NotBlank private final String puuid;

        @NotBlank @Size(min = 3, max = 16) private final String gameName;

        @NotBlank @Size(min = 3, max = 5) private final String tagLine;
        
        @JsonCreator
        public Response(@JsonProperty("puuid") String puuid,
                        @JsonProperty("gameName") String gameName,
                        @JsonProperty("tagLine") String tagLine) {
            this.puuid = puuid;
            this.gameName = gameName;
            this.tagLine = tagLine;
        }

        public static AccountDto.Response of(String puuid, String gameName, String tagLine) {
            return new AccountDto.Response(puuid, gameName, tagLine);
        }
    }
}
