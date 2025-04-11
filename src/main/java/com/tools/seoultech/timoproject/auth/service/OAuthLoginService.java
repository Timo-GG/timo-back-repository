package com.tools.seoultech.timoproject.auth.service;

import com.tools.seoultech.timoproject.auth.dto.AuthTokens;
import com.tools.seoultech.timoproject.auth.dto.LoginResponse;
import com.tools.seoultech.timoproject.auth.dto.OAuthInfoResponse;
import com.tools.seoultech.timoproject.auth.dto.OAuthLoginParams;
import com.tools.seoultech.timoproject.auth.jwt.JwtProvider;
import com.tools.seoultech.timoproject.auth.jwt.TokenCollection;
import com.tools.seoultech.timoproject.auth.jwt.TokenInfo;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthLoginService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtProvider jwtProvider;

    public LoginResponse login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);

        if (oAuthInfoResponse.getEmail() == null) {
            throw new IllegalArgumentException("Discord 계정에 이메일이 존재하지 않거나 비공개입니다.");
        }

        // 기존 회원이면 false, 신규 회원이면 true를 반환하기 위한 플래그
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuthInfoResponse.getEmail());
        boolean isNewUser = optionalMember.isEmpty();
        Member member = optionalMember.orElseGet(() -> createNewMember(oAuthInfoResponse));
        log.info("isNewUser: " + isNewUser);


        TokenCollection tokenCollection = jwtProvider.createTokenCollection(TokenInfo.from(member.getId()));

        return LoginResponse.builder()
                .accessToken(tokenCollection.getAccessToken())
                .refreshToken(tokenCollection.getRefreshToken())
                .isNewUser(isNewUser)
                .build();
    }

    private Member createNewMember(OAuthInfoResponse oAuthInfoResponse) {
        Member member = Member.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(memberService.randomCreateNickname())
                .profileImageId(memberService.randomCreateProfileImageId())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();
        return memberRepository.save(member);
    }
}
