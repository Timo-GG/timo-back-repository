package com.tools.seoultech.timoproject.admin.service;

import com.tools.seoultech.timoproject.admin.AdminLog;
import com.tools.seoultech.timoproject.admin.AdminLogRepository;
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
    private final AdminLogRepository adminLogRepository;

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
            //todo : 어드민 아이디 커스텀화 해야함
            AdminLog log = new AdminLog(AdminLog.EntityType.MEMBER, AdminLog.MethodType.DELETE, id.toString(), "admin");
            // 로그 기록
            // admin이 해당 댓글을 삭제했다는 로그를 남김
            adminLogRepository.save(log);
            memberRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + id);
        }
    }
}