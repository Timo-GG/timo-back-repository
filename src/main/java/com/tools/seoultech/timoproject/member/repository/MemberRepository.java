package com.tools.seoultech.timoproject.member.repository;

import com.tools.seoultech.timoproject.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByNickname(String nickName);

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

