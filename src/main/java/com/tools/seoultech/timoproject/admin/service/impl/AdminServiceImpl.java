package com.tools.seoultech.timoproject.admin.service.impl;

import com.tools.seoultech.timoproject.admin.service.AdminService;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private static final String ADMIN_PASSWORD = "1234";
    private final MemberRepository memberRepository;

    @Override
    public boolean authenticate(String username, String password) {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if ("ADMIN".equals(member.getRole().name()) && ADMIN_PASSWORD.equals(password)) {
                return true;
            }
        }
        return false;
    }
}
