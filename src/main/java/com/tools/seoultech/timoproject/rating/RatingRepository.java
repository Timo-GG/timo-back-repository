package com.tools.seoultech.timoproject.rating;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMemberId(Long memberId);
    long countByMemberId(Long memberId);

    Optional<Rating> findByMemberIdAndDuoIdAndMatchId(Long memberId, Long duoId, String matchId);

    boolean existsByMemberIdAndDuoIdAndMatchId(Long memberId, Long duoId, String matchId);

    @Query("SELECT r.duo.id, COUNT(r) > 0 FROM Rating r " +
            "WHERE r.member.id = :memberId AND r.duo.id IN :duoIds AND r.matchId IN :matchIds " +
            "GROUP BY r.duo.id")
    Map<Long, Boolean> findRatedMap(@Param("memberId") Long memberId,
                                    @Param("duoIds") List<Long> duoIds,
                                    @Param("matchIds") List<String> matchIds);
}
