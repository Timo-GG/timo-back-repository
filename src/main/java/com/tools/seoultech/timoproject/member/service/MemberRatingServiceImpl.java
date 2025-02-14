package com.tools.seoultech.timoproject.member.service;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.rating.Rating;
import com.tools.seoultech.timoproject.rating.RatingResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberRatingServiceImpl implements MemberRatingService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RatingResponse> getMemberRatings(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        // Member에 포함된 rating들을 활용하거나 별도로 조회
        List<Rating> ratings = member.getRatings();

        //ratings를 RatingResponse로 변환
        return ratings.stream()
                .map(rating -> new RatingResponse(rating.getId(), rating.getMember().getId(), rating.getDuo().getId(), rating.getScore(), rating.getAttitude(), rating.getSpeech(), rating.getSkill()))
                .toList();
    }
}
