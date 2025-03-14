package com.tools.seoultech.timoproject.rating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMemberId(Long memberId);
    long countByMemberId(Long memberId);

    Optional<Rating> findByMemberIdAndDuoIdAndMatchId(Long memberId, Long duoId, String matchId);

    boolean existsByMemberIdAndDuoIdAndMatchId(Long memberId, Long duoId, String matchId);
}
