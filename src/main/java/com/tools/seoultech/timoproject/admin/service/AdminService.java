package com.tools.seoultech.timoproject.admin.service;

import com.tools.seoultech.timoproject.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private static final String ADMIN_PASSWORD = "1234";
    private static final Logger log = LoggerFactory.getLogger(AdminService.class);
    private final MemberRepository memberRepository;

    /**
     * 아이디와 비밀번호를 검증하는 메서드
     *
     * @param username 입력받은 아이디
     * @param password 입력받은 비밀번호
     * @return 인증 성공 여부
     */
    public boolean authenticate(String username, String password) {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        // 사용자 존재 여부 및 ROLE_ADMIN 확인
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if ("ADMIN".equals(member.getRole().name()) && ADMIN_PASSWORD.equals(password)) {
                return true;
            }
        }

        return false;
    }
}

