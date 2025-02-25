package com.tools.seoultech.timoproject.post.repository;

import com.tools.seoultech.timoproject.post.domain.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Deprecated
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
