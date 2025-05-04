package com.tools.seoultech.timoproject.admin.service.impl;

import com.tools.seoultech.timoproject.admin.AdminLog;
import com.tools.seoultech.timoproject.admin.AdminLogRepository;
import com.tools.seoultech.timoproject.admin.service.AmemberService;
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
public class AmemberServiceImpl implements AmemberService {

    private final MemberRepository memberRepository;
    private final AdminLogRepository adminLogRepository;

    @Override
    public Page<Member> getList(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10); // 페이지 번호는 0부터 시작
        return memberRepository.findAll(pageable);
    }

    @Override
    public Member get(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + id));
    }

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public void delete(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            // 회원 이름(또는 표시할 별칭)을 "이름없음"으로 변경
            member.updateToDummy();
            // 추가적으로, 삭제 여부를 나타내는 플래그(isDeleted) 등을 설정할 수도 있음
            memberRepository.save(member);

            // 관리 로그 기록 (원래 삭제 로그를 남기던 부분)
            AdminLog log = new AdminLog(
                    AdminLog.EntityType.MEMBER,
                    AdminLog.MethodType.DELETE,
                    id.toString(),
                    "admin"
            );
            adminLogRepository.save(log);
        } else {
            throw new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + id);
        }
    }
}
