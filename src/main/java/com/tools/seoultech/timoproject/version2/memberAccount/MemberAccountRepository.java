package com.tools.seoultech.timoproject.version2.memberAccount;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAccountRepository extends JpaRepository<MemberAccount, Long> {
    boolean existsByUsername(String username);

    Optional<MemberAccount> findByEmail(String email);
}
