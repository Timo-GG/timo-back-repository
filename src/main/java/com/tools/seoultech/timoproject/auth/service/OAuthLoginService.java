package com.tools.seoultech.timoproject.auth.service;

import com.tools.seoultech.timoproject.auth.dto.LoginResponse;
import com.tools.seoultech.timoproject.auth.dto.OAuthInfoResponse;
import com.tools.seoultech.timoproject.auth.dto.OAuthLoginParams;
import com.tools.seoultech.timoproject.auth.dto.RiotInfoResponse;
import com.tools.seoultech.timoproject.auth.jwt.JwtProvider;
import com.tools.seoultech.timoproject.auth.jwt.TokenCollection;
import com.tools.seoultech.timoproject.auth.jwt.TokenInfo;
import com.tools.seoultech.timoproject.member.domain.OAuthProvider;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.Role;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        // Riot의 경우 특별 처리
        if (oAuthInfoResponse.getOAuthProvider() == OAuthProvider.RIOT) {
            return handleRiotLogin(oAuthInfoResponse);
        }

        // 다른 OAuth 제공자들은 이메일 필수
        if (oAuthInfoResponse.getEmail() == null) {
            throw new IllegalArgumentException(oAuthInfoResponse.getOAuthProvider().name() + " 계정에 이메일이 존재하지 않거나 비공개입니다.");
        }

        return handleEmailBasedLogin(oAuthInfoResponse);
    }

    private LoginResponse handleRiotLogin(OAuthInfoResponse oAuthInfoResponse) {
        RiotInfoResponse riotInfo = (RiotInfoResponse) oAuthInfoResponse;
        String riotSub = riotInfo.getSub();

        if (riotSub == null) {
            throw new IllegalArgumentException("Riot 계정 정보를 가져올 수 없습니다.");
        }

        // Riot용 가짜 이메일 생성 (고유성 보장)
        String fakeEmail = "riot" + Math.abs(riotSub.hashCode()) + "@timo.internal";

        // 기존 회원 찾기 (가짜 이메일로)
        Optional<Member> optionalMember = memberRepository.findByEmail(fakeEmail);
        boolean isNewUser = optionalMember.isEmpty();

        Member member;
        if (isNewUser) {
            // ✅ 신규 회원 생성 (RSO 계정 정보 포함)
            member = createNewRiotMember(riotInfo, fakeEmail);
        } else {
            // ✅ 기존 회원 업데이트 (RSO 계정 정보 갱신)
            member = optionalMember.get();
            updateMemberWithRSOInfo(member, riotInfo);
        }

        log.info("Riot RSO 로그인 - isNewUser: {}, PUUID: {}, GameName: {}",
                isNewUser, riotInfo.getPuuid(), riotInfo.getFullGameName());

        TokenCollection tokenCollection = jwtProvider.createTokenCollection(TokenInfo.from(member.getMemberId()));

        return LoginResponse.builder()
                .accessToken(tokenCollection.getAccessToken())
                .refreshToken(tokenCollection.getRefreshToken())
                .isNewUser(isNewUser)
                .build();
    }

    private LoginResponse handleEmailBasedLogin(OAuthInfoResponse oAuthInfoResponse) {
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuthInfoResponse.getEmail());
        boolean isNewUser = optionalMember.isEmpty();
        Member member = optionalMember.orElseGet(() -> createNewMember(oAuthInfoResponse));

        log.info("이메일 기반 로그인 - isNewUser: " + isNewUser);

        TokenCollection tokenCollection = jwtProvider.createTokenCollection(TokenInfo.from(member.getMemberId()));

        return LoginResponse.builder()
                .accessToken(tokenCollection.getAccessToken())
                .refreshToken(tokenCollection.getRefreshToken())
                .isNewUser(isNewUser)
                .build();
    }

    // ✅ 새로운 메서드: RSO 정보로 신규 회원 생성
    private Member createNewRiotMember(RiotInfoResponse riotInfo, String fakeEmail) {
        Member member = Member.builder()
                .email(fakeEmail)
                .username(memberService.randomCreateUsername())
                .oAuthProvider(OAuthProvider.RIOT)
                .role(Role.MEMBER)
                .build();

        // ✅ RSO로 인증된 계정 정보 설정
        if (riotInfo.getPuuid() != null && riotInfo.getGameName() != null) {
            member.updateRiotAccountWithRSO(
                    riotInfo.getPuuid(),
                    riotInfo.getGameName(),
                    riotInfo.getTagLine(),
                    riotInfo.getProfileUrl()
            );
            log.info("✅ 신규 회원 RSO 계정 정보 등록: {}#{}",
                    riotInfo.getGameName(), riotInfo.getTagLine());
        } else {
            log.warn("⚠️ RSO 계정 정보가 없어서 기본 회원으로만 생성");
        }

        return memberRepository.save(member);
    }

    // ✅ 새로운 메서드: 기존 회원 RSO 정보 업데이트
    private void updateMemberWithRSOInfo(Member member, RiotInfoResponse riotInfo) {
        // 기존 회원의 Riot 계정 정보를 RSO 정보로 업데이트
        if (riotInfo.getPuuid() != null && riotInfo.getGameName() != null) {
            member.updateRiotAccountWithRSO(
                    riotInfo.getPuuid(),
                    riotInfo.getGameName(),
                    riotInfo.getTagLine(),
                    riotInfo.getProfileUrl()
            );
            memberRepository.save(member);
            log.info("✅ 기존 회원 RSO 계정 정보 업데이트: {}#{}",
                    riotInfo.getGameName(), riotInfo.getTagLine());
        } else {
            log.warn("⚠️ RSO 계정 정보가 없어서 업데이트하지 않음");
        }
    }

    private Member createNewMember(OAuthInfoResponse oAuthInfoResponse) {
        Member member = Member.builder()
                .email(oAuthInfoResponse.getEmail())
                .username(memberService.randomCreateUsername())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .role(Role.MEMBER)
                .build();
        return memberRepository.save(member);
    }
}