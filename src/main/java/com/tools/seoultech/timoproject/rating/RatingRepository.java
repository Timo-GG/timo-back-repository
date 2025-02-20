package com.tools.seoultech.timoproject.rating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMemberId(Long memberId);
    long countByMemberId(Long memberId);

}
