package com.tools.seoultech.timoproject.riot;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "https://americas.api.riotgames.com", accept = MediaType.APPLICATION_JSON_VALUE)
public interface RiotRSOApiClient {

    /**
     * RSO 인증된 사용자의 계정 정보 조회
     * OAuth Bearer 토큰으로 인증
     */
    @GetExchange("/riot/account/v1/accounts/me")
    RSOAccountResponse getMyAccount(
            @RequestHeader("Authorization") String bearerToken
    );

    /**
     * RSO 계정 응답 DTO
     */
    record RSOAccountResponse(
            String puuid,
            String gameName,
            String tagLine
    ) {
        public String getFullGameName() {
            return gameName + "#" + tagLine;
        }

        public String getProfileUrl() {
            return "https://tracker.gg/valorant/profile/riot/" +
                    gameName + "%23" + tagLine + "/overview";
        }
    }
}
