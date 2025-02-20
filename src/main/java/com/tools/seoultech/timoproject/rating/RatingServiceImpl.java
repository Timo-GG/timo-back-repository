package com.tools.seoultech.timoproject.rating;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.rating.dto.RatingRequest;
import com.tools.seoultech.timoproject.rating.dto.RatingResponse;
import com.tools.seoultech.timoproject.rating.dto.RatingTotalResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService{

        private final RatingRepository ratingRepository;
        private final MemberRepository memberRepository;

        @Override
        public RatingResponse saveRating(RatingRequest RatingRequest) {

            Member member = memberRepository.findById(2L).orElseThrow(()
                    -> new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
            Member duo = memberRepository.findById(3L).orElseThrow(()
                    -> new BusinessException(ErrorCode.NOT_FOUND_DUO_EXCEPTION));

            Rating rating = Rating.builder()
                    .score(RatingRequest.score())
                    .attitude(RatingRequest.attitude())
                    .speech(RatingRequest.speech())
                    .skill(RatingRequest.skill())
                    .member(member)
                    .duo(duo)
                    .build();


            Rating saved = ratingRepository.save(rating);
            return new RatingResponse(saved.getId(), saved.getMember().getId(), saved.getDuo().getId(), saved.getScore(), saved.getAttitude(), saved.getSpeech(), saved.getSkill());


        }

        @Override
        public void deleteRating(Long id) {
            ratingRepository.deleteById(id);
        }
    // todo 로그인한 내 정보 (ex. email)로 멤버 가져와서 해야됨
    @Override
    public RatingTotalResponse getRatings(Long memberId) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION));

        List<Rating> ratings = ratingRepository.findByMemberId(member.getId());

        List<RatingResponse> ratingResponses = ratings.stream()
                .map(rating -> new RatingResponse(
                        rating.getId(),
                        rating.getMember().getId(),
                        rating.getDuo().getId(),
                        rating.getScore(),
                        rating.getAttitude(),
                        rating.getSpeech(),
                        rating.getSkill()))
                .toList();

        RatingTotalResponse ratingTotalResponse = new RatingTotalResponse(Rating.calculateAverageRatings(ratings), ratingResponses);

        return ratingTotalResponse;
    }

    @Override
    public BigDecimal getRatingAverage(Long memberId) {
            List<Rating> ratings = ratingRepository.findByMemberId(memberId);
            return Rating.calculateAverageRatings(ratings);
    }
}
