package com.tools.seoultech.timoproject.match.service;

import com.tools.seoultech.timoproject.match.domain.MatchingOption;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionResponse;
import com.tools.seoultech.timoproject.match.repository.MatchingOptionRepository;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingOptionService {

    private final MatchingOptionRepository matchingOptionRepository;
    private final MemberRepository memberRepository;

    // 매칭 옵션 생성
    public MatchingOptionResponse createMatchingOption(Long memberId, MatchingOptionRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        MatchingOption matchingOption = MatchingOption.builder()
                .introduce(request.getIntroduce())
                .age(request.getAge())
                .gender(request.getGender())
                .voiceChat(request.getVoiceChat())
                .playStyle(request.getPlayStyle())
                .playTime(request.getPlayTime())
                .gameMode(request.getGameMode())
                .member(member)
                .build();

        matchingOptionRepository.save(matchingOption);
        return new MatchingOptionResponse(matchingOption);
    }

    // 특정 회원의 매칭 옵션 조회
    public MatchingOptionResponse getMatchingOption(Long memberId) {
        MatchingOption matchingOption = matchingOptionRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("MatchingOption not found for Member ID: " + memberId));

        return new MatchingOptionResponse(matchingOption);
    }

    // 매칭 옵션 수정
    public MatchingOptionResponse updateMatchingOption(Long memberId, MatchingOptionRequest requestDto) {
        MatchingOption matchingOption = matchingOptionRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("MatchingOption not found for Member ID: " + memberId));
        matchingOption.update(requestDto);
        matchingOptionRepository.save(matchingOption);

        return new MatchingOptionResponse(matchingOption);
    }

    // 매칭 옵션 삭제
    public void deleteMatchingOption(Long memberId) {
        MatchingOption matchingOption = matchingOptionRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("MatchingOption not found for Member ID: " + memberId));

        matchingOptionRepository.delete(matchingOption);
    }
}
