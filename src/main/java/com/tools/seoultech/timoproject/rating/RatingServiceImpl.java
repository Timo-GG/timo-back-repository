package com.tools.seoultech.timoproject.rating;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.rating.dto.RatingRequest;
import com.tools.seoultech.timoproject.rating.dto.RatingResponse;
import com.tools.seoultech.timoproject.rating.dto.RatingTotalResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService{

        private final RatingRepository ratingRepository;
        private final MemberRepository memberRepository;

        @Override
        public RatingResponse saveRating(RatingRequest RatingRequest) {

            Member member = memberRepository.findById(2L).orElseThrow(()
                    -> new EntityNotFoundException("Member not found with id: " + 2L));
            Member duo = memberRepository.findById(3L).orElseThrow(()
                    -> new EntityNotFoundException("Member not found with id: " + 3L));

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
        // 해당 멤버가 존재하는지 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));

        // 해당 멤버의 Rating 리스트 가져오기
        List<Rating> ratings = ratingRepository.findByMemberId(memberId);

        // Rating -> RatingResponse 변환
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
}
