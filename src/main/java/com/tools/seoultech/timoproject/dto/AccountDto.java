package com.tools.seoultech.timoproject.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class AccountDto {
    @Getter
    public static class Request{
        @NotBlank private final String gameName;
        @NotBlank private final String tagLine;

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
