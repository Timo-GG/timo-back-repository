package com.tools.seoultech.timoproject.post.repository;

import com.tools.seoultech.timoproject.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
