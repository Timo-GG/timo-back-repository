package com.tools.seoultech.timoproject.version2.memberAccount;

import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAccountRepository extends JpaRepository<MemberAccount, Long> {
    boolean existsByUserName(String username);

}
