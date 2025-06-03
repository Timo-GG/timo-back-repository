package com.tools.seoultech.timoproject.review;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.repository.PageRepository;
import com.tools.seoultech.timoproject.member.MemberRepository;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final MemberRepository memberRepository;
    private final PageRepository pageRepository;
    private final EntityManager entityManager;

    @Transactional
    public Review createReview(Long myPageId, Long currentMemberId, ReviewRequestDto dto) {
        MyPage myPage = pageRepository.findById(myPageId)
                .orElseThrow(() -> new RuntimeException("MyPage not found"));

        Boolean isAcceptor = myPage.getAcceptor().getMemberId().equals(currentMemberId);
        Review review = dto.toEntity();
        MyPage updatedReview = myPage.updateReview(review, isAcceptor);
        entityManager.merge(updatedReview);
        return review;
    }

    @Transactional(readOnly = true)
    public Review getReviews(Long myPageId, Long currentMemberId) {
        MyPage myPage = pageRepository.findById(myPageId)
                .orElseThrow(() -> new RuntimeException("MyPage not found"));
        Boolean isAcceptor = myPage.getAcceptor().getMemberId().equals(currentMemberId);
        return isAcceptor ? myPage.getAcceptorReview() : myPage.getRequestorReview();
    }
}


