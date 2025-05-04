package com.tools.seoultech.timoproject.auth.service;

import com.tools.seoultech.timoproject.auth.dto.AuthTokens;
import com.tools.seoultech.timoproject.auth.dto.LoginResponse;
import com.tools.seoultech.timoproject.auth.dto.OAuthInfoResponse;
import com.tools.seoultech.timoproject.auth.dto.OAuthLoginParams;
import com.tools.seoultech.timoproject.auth.jwt.JwtProvider;
import com.tools.seoultech.timoproject.auth.jwt.TokenCollection;
import com.tools.seoultech.timoproject.auth.jwt.TokenInfo;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.domain.Role;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.version2.memberAccount.MemberAccountRepository;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthLoginService {

    private final MemberAccountRepository memberAccountRepository;
    private final MemberService memberService;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtProvider jwtProvider;

    public LoginResponse login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);

        if (oAuthInfoResponse.getEmail() == null) {
            throw new IllegalArgumentException("Discord 계정에 이메일이 존재하지 않거나 비공개입니다.");
        }

        // 기존 회원이면 false, 신규 회원이면 true를 반환하기 위한 플래그
        Optional<MemberAccount> optionalMember = memberAccountRepository.findByEmail(oAuthInfoResponse.getEmail());
        boolean isNewUser = optionalMember.isEmpty();
        MemberAccount member = optionalMember.orElseGet(() -> createNewMember(oAuthInfoResponse));
        log.info("isNewUser: " + isNewUser);


        TokenCollection tokenCollection = jwtProvider.createTokenCollection(TokenInfo.from(member.getMemberId()));

        return LoginResponse.builder()
                .accessToken(tokenCollection.getAccessToken())
                .refreshToken(tokenCollection.getRefreshToken())
                .isNewUser(isNewUser)
                .build();
    }

    private MemberAccount createNewMember(OAuthInfoResponse oAuthInfoResponse) {
        MemberAccount member = MemberAccount.builder()
                .email(oAuthInfoResponse.getEmail())
                .username(memberService.randomCreateUsername())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .role(Role.MEMBER)
                .build();
        return memberAccountRepository.save(member);
    }
}
