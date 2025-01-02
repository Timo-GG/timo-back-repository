package com.tools.seoultech.timoproject.member.service;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.dto.NaverResponse;
import com.tools.seoultech.timoproject.member.dto.OAuth2Member;
import com.tools.seoultech.timoproject.member.dto.OAuth2Response;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            oAuth2Response = null;
            return null;
        }

        // 회원 식별자
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        Member member = memberRepository.findByUsername(username)
                .map(existingMember -> { // 가입 여부 파악
                    existingMember.update(oAuth2Response.getEmail());
                    return existingMember;
                })
                .orElseGet(() -> Member.create(username, oAuth2Response.getEmail(), "ROLE_USER"));

        memberRepository.save(member);
        return new OAuth2Member(oAuth2Response, member.getRole());
    }
}