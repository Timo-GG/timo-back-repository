package com.tools.seoultech.timoproject.policy.repository;

import com.tools.seoultech.timoproject.policy.domain.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long > {
    void deleteByMemberId(Long memberId);
    boolean existsByMemberIdAndRegDateBefore(Long memberId, LocalDateTime now);
}
