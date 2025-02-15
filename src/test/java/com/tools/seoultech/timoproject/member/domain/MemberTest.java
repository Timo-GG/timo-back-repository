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
    private SocialAccount socialAccount;

    @BeforeEach
    void init() {
        member = Member.builder()
                .email("test@example.com")
                .username("testUser")
                .build();

        socialAccount = SocialAccount.builder()
                .provider("NAVER")
                .providerId("12345")
                .build();

        memberRepository.save(member);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("회원이 소셜 계정을 정상적으로 연동할 수 있다.")
    void linkSocialAccount_Success() {
        // given
        Member foundMember = memberRepository.findByEmail("test@example.com").orElseThrow();

        // when
        foundMember.linkSocialAccount(socialAccount);

        // then
        assertThat(foundMember.getSocialAccounts()).contains(socialAccount);
        assertThat(foundMember.getSocialAccounts().get(0).getProvider()).isEqualTo("NAVER");
        assertThat(socialAccount.getMember()).isEqualTo(foundMember);
    }
}
