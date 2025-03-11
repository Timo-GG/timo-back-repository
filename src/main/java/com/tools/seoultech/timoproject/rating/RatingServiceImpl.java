package com.tools.seoultech.timoproject.rating;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.rating.dto.RatingRequest;
import com.tools.seoultech.timoproject.rating.dto.RatingResponse;
import com.tools.seoultech.timoproject.rating.dto.RatingTotalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public RatingResponse saveRating(Long memberId, RatingRequest ratingRequest) {
        // 1. 채팅방 종료 여부 검증: matchId를 이용하여 ChatRoom 조회
        validateChatRoomTerminated(ratingRequest);

        // 2. 중복 평점 제출 여부 검증: (member, duo, matchId) 복합키 기준
        validateNotDuplicatedRating(memberId, ratingRequest);

        // 3. 회원 및 듀오 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
        Member duo = memberRepository.findById(ratingRequest.duoId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DUO_EXCEPTION));

        // 4. 평점 엔티티 생성 및 저장
        Rating rating = Rating.builder()
                .score(ratingRequest.score())
                .attitude(ratingRequest.attitude())
                .speech(ratingRequest.speech())
                .skill(ratingRequest.skill())
                .member(member)
                .duo(duo)
                .matchId(ratingRequest.matchId())
                .build();

        Rating saved = ratingRepository.save(rating);
        return mapToRatingResponse(saved);
    }

    @Override
    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }

    @Override
    public RatingTotalResponse getRatings(Long memberId) {
        // 회원 존재 여부 검증 (불필요하다면 생략 가능)
        memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION));

        List<Rating> ratings = ratingRepository.findByMemberId(memberId);
        List<RatingResponse> ratingResponses = ratings.stream()
                .map(this::mapToRatingResponse)
                .toList();
        BigDecimal average = Rating.calculateAverageRatings(ratings);
        return new RatingTotalResponse(average, ratingResponses);
    }

    @Override
    public BigDecimal getRatingAverage(Long memberId) {
        List<Rating> ratings = ratingRepository.findByMemberId(memberId);
        return Rating.calculateAverageRatings(ratings);
    }

    private RatingResponse mapToRatingResponse(Rating rating) {
        return new RatingResponse(
                rating.getId(),
                rating.getMember().getId(),
                rating.getDuo().getId(),
                rating.getScore(),
                rating.getAttitude(),
                rating.getSpeech(),
                rating.getSkill(),
                rating.getMatchId()
        );
    }

    private void validateNotDuplicatedRating(Long memberId, RatingRequest ratingRequest) {
        Optional<Rating> existingRating = ratingRepository.findByMemberIdAndDuoIdAndMatchId(
                memberId, ratingRequest.duoId(), ratingRequest.matchId());
        if (existingRating.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_RATING_EXCEPTION);
        }
    }

    private void validateChatRoomTerminated(RatingRequest ratingRequest) {
        ChatRoom chatRoom = chatRoomRepository.findByMatchId(ratingRequest.matchId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION));
        if (!chatRoom.isTerminated()) {
            throw new BusinessException(ErrorCode.CHATROOM_NOT_TERMINATED_EXCEPTION);
        }
    }
}

