package com.tools.seoultech.timoproject.review;
import com.tools.seoultech.timoproject.memberAccount.MemberAccountRepository;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberAccountRepository memberAccountRepository;

    @Transactional
    public ReviewResponseDto createReview(Long currentMemberId, ReviewRequestDto dto) {
        MemberAccount reviewer = memberAccountRepository.findById(currentMemberId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        MemberAccount reviewee = memberAccountRepository.findById(dto.revieweeId())
                .orElseThrow(() -> new RuntimeException("Reviewee not found"));

        Review review = dto.toEntity(reviewer, reviewee);
        Review saved = reviewRepository.save(review);

        return ReviewResponseDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByReviewee(Long revieweeId) {
        MemberAccount reviewee = memberAccountRepository.findById(revieweeId)
                .orElseThrow(() -> new RuntimeException("Reviewee not found"));

        return reviewRepository.findByReviewee(reviewee).stream()
                .map(ReviewResponseDto::fromEntity)
                .toList();
    }
}


