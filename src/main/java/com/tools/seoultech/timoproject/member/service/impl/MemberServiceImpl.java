package com.tools.seoultech.timoproject.member.service.impl;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
