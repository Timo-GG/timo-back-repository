package com.tools.seoultech.timoproject.match.repository;

import com.tools.seoultech.timoproject.match.domain.MatchingOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchingOptionRepository extends JpaRepository<MatchingOption, Long> {

    Optional<MatchingOption> findByMemberId(Long memberId);
}
