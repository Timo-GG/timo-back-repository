package com.tools.seoultech.timoproject.version2.ranking.service;

import com.tools.seoultech.timoproject.version2.ranking.RankingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingInfoRepository extends JpaRepository<RankingInfo, Long> {
}
