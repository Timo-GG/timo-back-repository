package com.tools.seoultech.timoproject.member.service;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.domain.SocialAccount;
import com.tools.seoultech.timoproject.member.dto.NaverResponse;
import com.tools.seoultech.timoproject.member.dto.OAuth2Member;
import com.tools.seoultech.timoproject.member.dto.OAuth2Response;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // TODO : 현재는 네이버만 구현해서 추후에 타 플랫폼도  추가 에정
        OAuth2Response oAuth2Response = new NaverResponse(attributes);
        Member member = getMember(oAuth2Response.getProvider(), oAuth2Response.getProviderId(),
                oAuth2Response.getEmail(), oAuth2Response.getName());
        log.info((oAuth2Response.getEmail()));

        return new OAuth2Member(oAuth2Response, member.getRole());
    }

    public Member getMember(String provider, String providerId, String email, String nickname) {
        return memberRepository.findBySocialAccount(provider, providerId)
                .orElseGet(() -> linkOrRegister(provider, providerId, email, nickname));
    }

    private Member linkOrRegister(String provider, String providerId, String email, String nickname) {

        // 소셜 정보로 멤버를 찾지 못한 경우, 이메일 정보와 일치하는 멤버를 찾아 소셜 정보 등록
        SocialAccount socialAccount = SocialAccount.builder()
                .provider(provider)
                .providerId(providerId)
                .build();

        return memberRepository.findByEmail(email)
                .map(member -> {
                    // 이메일 회원 정보가 존재하면, 소셜 연동 진행
                    member.linkSocialAccount(socialAccount);
                    return member;
                })
                // 새로운 유저 회원가입 진행
                .orElseGet(() -> register(socialAccount, email, nickname));
    }

    private Member register(SocialAccount socialAccount, String email, String nickname) {

        // 새로운 유저 회원가입
        Member member = Member.builder()
                .email(email)
                .username(nickname)
                .role("ROLE_USER")
                .build();

        member.linkSocialAccount(socialAccount);
        memberRepository.save(member);
        return member;
    }
}