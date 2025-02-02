package com.tools.seoultech.timoproject.match.service;

import com.tools.seoultech.timoproject.match.domain.DuoInfo;
import com.tools.seoultech.timoproject.match.domain.UserInfo;
import com.tools.seoultech.timoproject.match.dto.DuoInfoRequest;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionResponse;
import com.tools.seoultech.timoproject.match.dto.UserInfoRequest;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingOptionService {

    private final MemberRepository memberRepository;

    @Transactional
    public MatchingOptionResponse updateMatchingOption(Long memberId, MatchingOptionRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        // UserInfo 엔티티 생성
        UserInfoRequest userInfoReq = request.getUserInfo();
        UserInfo userInfo = UserInfo.builder()
                .introduce(userInfoReq.getIntroduce())
                .age(userInfoReq.getAge())
                .gender(userInfoReq.getGender())
                .playPosition(userInfoReq.getPlayPosition())
                .playCondition(userInfoReq.getPlayCondition())
                .voiceChat(userInfoReq.getVoiceChat())
                .playStyle(userInfoReq.getPlayStyle())
                .playTime(userInfoReq.getPlayTime())
                .gameMode(userInfoReq.getGameMode())
                .build();

        // DuoInfo 엔티티 생성
        DuoInfoRequest duoInfoReq = request.getDuoInfo();
        DuoInfo duoInfo = DuoInfo.builder()
                .duoPlayPosition(duoInfoReq.getDuoPlayPosition())
                .duoPlayTime(duoInfoReq.getDuoPlayTime())
                .duoVoiceChat(duoInfoReq.getDuoVoiceChat())
                .duoAge(duoInfoReq.getDuoAge())
                .build();

        // Member의 메칭 설정 업데이트
        member.updateMatchOption(userInfo, duoInfo);
        return new MatchingOptionResponse(member);
    }

    @Transactional(readOnly = true)
    public MatchingOptionResponse getMatchingOption(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        return new MatchingOptionResponse(member);
    }
}
