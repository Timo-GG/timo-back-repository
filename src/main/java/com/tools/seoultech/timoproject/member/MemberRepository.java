package com.tools.seoultech.timoproject.member;

import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.RiotVerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);

    Optional<Member> findByEmail(String email);

    boolean existsByRiotAccount_Puuid(String puuid);

    boolean existsByCertifiedUnivInfo_UnivCertifiedEmail(String email);

    boolean existsByRiotAccount_PuuidAndMemberIdNot(String puuid, Long memberId);

    @Query("SELECT m FROM Member m JOIN FETCH m.riotAccount WHERE m.riotAccount.puuid IS NOT NULL")
    List<Member> findAllWithRiotAccount();

    boolean existsByRiotAccount_PuuidAndRiotAccount_VerificationTypeAndMemberIdNot(String puuid, RiotVerificationType riotVerificationType, Long memberId);
}
