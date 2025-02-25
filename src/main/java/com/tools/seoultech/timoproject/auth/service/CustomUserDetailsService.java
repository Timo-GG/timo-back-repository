package com.tools.seoultech.timoproject.auth.service;

import com.tools.seoultech.timoproject.auth.domain.CustomUserDetails;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return CustomUserDetails.from(
                memberRepository.findById(Long.parseLong(username))
                        .orElseThrow(() -> {
                            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
                        })
        );
    }
}
