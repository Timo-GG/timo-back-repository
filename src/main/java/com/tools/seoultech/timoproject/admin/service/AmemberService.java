package com.tools.seoultech.timoproject.admin.service;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AmemberService {

    private final MemberRepository memberRepository;

    // 회원 목록 조회 (페이지네이션)
    public Page<Member> getList(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10); // 페이지 번호는 0부터 시작
        return memberRepository.findAll(pageable);
    }

    // 특정 회원 조회
    public Member get(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + id));
    }

    // 회원 저장
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    // 회원 삭제
    public void delete(Long id) {
        if (memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + id);
        }
    }
}