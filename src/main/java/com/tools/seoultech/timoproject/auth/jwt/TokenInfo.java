package com.tools.seoultech.timoproject.auth.jwt;

import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenInfo {

    private Map<String, Object> payload;

    public static TokenInfo from(MemberAccount member) {
        Map<String, Object> accessTokenPayload = new HashMap<>();
        accessTokenPayload.put("memberId", member.getMemberId());
        return new TokenInfo(accessTokenPayload);
    }

    public static TokenInfo from(Long id) {
        Map<String, Object> accessTokenPayload = new HashMap<>();
        accessTokenPayload.put("memberId", id);
        return new TokenInfo(accessTokenPayload);
    }
}
