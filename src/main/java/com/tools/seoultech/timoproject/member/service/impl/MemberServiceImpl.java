package com.tools.seoultech.timoproject.member.service.impl;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    @Override
    public Member getById(Long memberId) {
        return memberRepository.findById(memberId).
                orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));
    }

    public boolean checkNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public String randomCreateNickname() {
        return "티모대위" + "-" + UUID.randomUUID().toString().substring(0, 5);
    }

    @Override
    public Integer randomCreateProfileImageId() {
        // 1~6 범위의 랜덤 숫자 생성
        return (int) (Math.random() * 6) + 1;
    }

    @Transactional
    public Member updateAdditionalInfo(Long memberId, String nickname, String playerName, String playerTag) {
        Member member = getById(memberId);

        // 1) 닉네임 중복 체크
        if (checkNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 2) 엔티티 업데이트
        member.updateNickname(nickname);
        member.linkRiotInfo(playerName, playerTag);

        // 3) 저장 (영속성 컨텍스트에서 변경 감지로 자동 반영)
        return member;
    }

    @Override
    public Integer updateProfileImageId(Long memberId, Integer imageId) {
        Member member = getById(memberId);
        member.updateProfileImageId(imageId);
        return imageId;
    }
}
