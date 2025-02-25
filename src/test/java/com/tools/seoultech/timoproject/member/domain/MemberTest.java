package com.tools.seoultech.timoproject.member.domain;

import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Member member;

    @BeforeEach
    void init() {
        member = Member.builder()
                .email("test@example.com")
                .username("testUser")
                .oAuthProvider(OAuthProvider.NAVER)
                .build();

        memberRepository.save(member);
        entityManager.flush();
        entityManager.clear();
    }
}
