package com.tools.seoultech.timoproject.ranking;

import com.tools.seoultech.timoproject.ranking.dto.RedisRankingInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingInfoRedisRepository extends CrudRepository<RedisRankingInfo, String> {
}
