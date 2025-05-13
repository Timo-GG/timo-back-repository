package com.tools.seoultech.timoproject.review;

import com.tools.seoultech.timoproject.memberAccount.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewee(Member reviewee);
}
