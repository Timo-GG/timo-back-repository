package com.tools.seoultech.timoproject.review;
import com.tools.seoultech.timoproject.memberAccount.MemberRepository;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewResponseDto createReview(Long currentMemberId, ReviewRequestDto dto) {
        Member reviewer = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        Member reviewee = memberRepository.findById(dto.revieweeId())
                .orElseThrow(() -> new RuntimeException("Reviewee not found"));

        Review review = dto.toEntity(reviewer, reviewee);
        Review saved = reviewRepository.save(review);

        return ReviewResponseDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByReviewee(Long revieweeId) {
        Member reviewee = memberRepository.findById(revieweeId)
                .orElseThrow(() -> new RuntimeException("Reviewee not found"));

        return reviewRepository.findByReviewee(reviewee).stream()
                .map(ReviewResponseDto::fromEntity)
                .toList();
    }
}


