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

//    @Query("select m from Member m "
//            + "left join fetch m.memberInfo ui "
//            + "left join fetch ui.memberInfoSkills uis "
//            + "where m.id = :memberId")
//    Optional<Member> findWithInfo(Long memberId);
//
//    @Query("select m from Member m "
//            + "left join fetch m.memberInfo ui "
//            + "left join fetch m.likeUsers lu "
//            + "where m.id = :memberId")
//    Optional<Member> findWithLikeUsers(Long memberId);
}

