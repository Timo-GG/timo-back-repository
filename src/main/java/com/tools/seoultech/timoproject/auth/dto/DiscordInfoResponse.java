package com.tools.seoultech.timoproject.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tools.seoultech.timoproject.memberAccount.domain.OAuthProvider;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscordInfoResponse implements OAuthInfoResponse {

    @JsonProperty("email")
    private String email;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.DISCORD;
    }
}

