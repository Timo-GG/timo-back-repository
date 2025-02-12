package com.tools.seoultech.timoproject.admin.service;

import com.tools.seoultech.timoproject.member.domain.Member;
import org.springframework.data.domain.Page;

public interface AmemberService {
    // 회원 목록 조회 (페이지네이션)
    Page<Member> getList(int pageNumber);

    // 특정 회원 조회
    Member get(Long id);

    // 회원 저장
    Member save(Member member);

    // 회원 삭제
    void delete(Long id);
}
