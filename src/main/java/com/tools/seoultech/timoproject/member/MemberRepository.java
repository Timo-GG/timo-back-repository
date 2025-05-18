package com.tools.seoultech.timoproject.member;

import com.tools.seoultech.timoproject.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);

    Optional<Member> findByEmail(String email);

    boolean existsByRiotAccount_Puuid(String puuid);

    boolean existsByCertifiedUnivInfo_UnivCertifiedEmail(String email);

    boolean existsByRiotAccount_PuuidAndMemberIdNot(String puuid, Long memberId);

}
