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
public class MatchingOptionServiceImpl implements MatchingOptionService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MatchingOptionResponse updateMatchingOption(Long memberId, MatchingOptionRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        // UserInfo 엔티티 생성
        UserInfo userInfo = request.toUserInfo();

        // DuoInfo 엔티티 생성
        DuoInfo duoInfo = request.toDuoInfo();

        // Member의 메칭 설정 업데이트
        member.updateMatchOption(userInfo, duoInfo);
        return new MatchingOptionResponse(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MatchingOptionResponse getMatchingOption(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        return new MatchingOptionResponse(member);
    }
}
