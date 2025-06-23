package com.tools.seoultech.timoproject.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tools.seoultech.timoproject.member.domain.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class RiotInfoResponse implements OAuthInfoResponse {

    @JsonProperty("sub")
    private String sub; // Riot 고유 ID

    @JsonProperty("email")
    private String email;

    @JsonProperty("email_verified")
    private boolean emailVerified;

    @JsonProperty("phone_number_verified")
    private boolean phoneNumberVerified;

    @JsonProperty("ban")
    private Object ban; // 밴 정보 (복잡한 객체이므로 Object로 처리)

    @JsonProperty("dat")
    private Object dat; // 데이터 정보

    @JsonProperty("lol")
    private Object lol; // LOL 관련 정보

    @JsonProperty("acct")
    private RiotAccount acct; // 계정 정보

    // ✅ 추가: RSO 계정 정보
    private RiotAccountInfo accountInfo;

    @Override
    public String getEmail() {
        return email;
    }

    public String getSub() {
        return sub;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.RIOT;
    }

    public RiotInfoResponse withAccountInfo(RiotAccountInfo accountInfo) {
        this.accountInfo = accountInfo;
        return this;
    }

    // ✅ 추가: RSO 계정 정보 접근 메서드들
    public String getPuuid() {
        return accountInfo != null ? accountInfo.getPuuid() : null;
    }

    public String getGameName() {
        return accountInfo != null ? accountInfo.getGameName() : null;
    }

    public String getTagLine() {
        return accountInfo != null ? accountInfo.getTagLine() : null;
    }

    public String getProfileUrl() {
        return accountInfo != null ? accountInfo.getProfileIconUrl() : null;
    }

    public String getFullGameName() {
        return accountInfo != null ? accountInfo.getFullGameName() : null;
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class RiotAccount {

        @JsonProperty("type")
        private int type;

        @JsonProperty("state")
        private String state;

        @JsonProperty("adm")
        private boolean adm;

        @JsonProperty("country")
        private String country;

        @JsonProperty("game_name")
        private String gameName;

        @JsonProperty("tag_line")
        private String tagLine;

        @JsonProperty("created_at")
        private long createdAt;
    }
}