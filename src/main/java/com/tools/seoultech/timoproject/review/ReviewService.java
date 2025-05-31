package com.tools.seoultech.timoproject.review;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.repository.PageRepository;
import com.tools.seoultech.timoproject.member.MemberRepository;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PageRepository pageRepository;

    @Transactional
    public ReviewResponseDto createReview(Long currentMemberId, ReviewRequestDto dto) {
        Member reviewer = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        Member reviewee = memberRepository.findById(dto.revieweeId())
                .orElseThrow(() -> new RuntimeException("Reviewee not found"));
        MyPage myPage = pageRepository.findById(dto.mypageId())
                .orElseThrow(() -> new RuntimeException("MyPage not found"));
        Review review = dto.toEntity(reviewer, reviewee, myPage);
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


