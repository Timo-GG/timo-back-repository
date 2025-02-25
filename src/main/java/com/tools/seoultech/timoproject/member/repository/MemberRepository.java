package com.tools.seoultech.timoproject.member.repository;

import com.tools.seoultech.timoproject.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
            SELECT m from Member m
            join m.socialAccounts sa
            WHERE sa.provider = :provider AND sa.providerId = :providerId
            """)
    Optional<Member> findBySocialAccount(String provider, String providerId);

    Optional<Member> findByUsername(String username);
}

