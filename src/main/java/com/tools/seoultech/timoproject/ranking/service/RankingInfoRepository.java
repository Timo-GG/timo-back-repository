package com.tools.seoultech.timoproject.ranking.service;

import com.tools.seoultech.timoproject.ranking.RankingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankingInfoRepository extends JpaRepository<RankingInfo, Long> {
    Optional<RankingInfo> findByMemberMemberId(Long memberId);

    boolean existsByMemberMemberId(Long memberId);
}
