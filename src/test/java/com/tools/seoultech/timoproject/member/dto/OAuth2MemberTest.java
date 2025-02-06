package com.tools.seoultech.timoproject.member.dto;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.domain.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OAuth2MemberTest {

    Member member;

    @BeforeEach
    void init(){

        member = Member.builder()
                .email("asdf123@gmail.com")
                .username("test")
                .build();
    }
    @Test
    void getAuthorities() {
        Assertions.assertThat(member.getRole()).isEqualTo(Role.MEMBER);
    }
}