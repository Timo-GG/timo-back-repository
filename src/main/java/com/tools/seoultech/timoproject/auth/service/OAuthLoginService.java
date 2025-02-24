package com.tools.seoultech.timoproject.auth.service;

import com.tools.seoultech.timoproject.auth.dto.AuthTokens;
import com.tools.seoultech.timoproject.auth.dto.OAuthInfoResponse;
import com.tools.seoultech.timoproject.auth.dto.OAuthLoginParams;
import com.tools.seoultech.timoproject.auth.jwt.JwtProvider;
import com.tools.seoultech.timoproject.auth.jwt.TokenCollection;
import com.tools.seoultech.timoproject.auth.jwt.TokenInfo;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final MemberRepository memberRepository;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtProvider jwtProvider;

    public TokenCollection login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        return jwtProvider.createTokenCollection(TokenInfo.from(memberId));
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        Member member = Member.builder()
                .email(oAuthInfoResponse.getEmail())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();

        if(member.getNickname() == null){
            member.randomCreateNickname();
        }

        return memberRepository.save(member).getId();
    }
}