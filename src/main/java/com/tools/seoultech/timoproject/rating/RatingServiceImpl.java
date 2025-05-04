package com.tools.seoultech.timoproject.rating;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomMemberRepository;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.rating.dto.DuoResponse;
import com.tools.seoultech.timoproject.rating.dto.RatingRequest;
import com.tools.seoultech.timoproject.rating.dto.RatingResponse;
import com.tools.seoultech.timoproject.rating.dto.RatingTotalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository; // 가정

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

    @Override
    public boolean hasRated(Long memberId, Long duoId, String matchId) {
        return ratingRepository.existsByMemberIdAndDuoIdAndMatchId(memberId, duoId, matchId);
    }

    @Override
    public List<DuoResponse> getDuoList(Long memberId) {
        // (1) 해당 member가 참여했던 모든 ChatRoomMember 엔티티 조회 (듀오 포함)
        List<ChatRoomMember> myChatRoomMembers = chatRoomMemberRepository.findByMemberIdWithDuo(memberId);

        // 💡 (1) 모든 듀오 ID 및 matchId를 한 번에 가져오기
        List<Long> duoIds = myChatRoomMembers.stream()
                .map(ChatRoomMember::getMember)
                .map(Member::getId)
                .distinct()
                .toList();

        List<String> matchIds = myChatRoomMembers.stream()
                .map(crm -> crm.getChatRoom().getMatchId())
                .distinct()
                .toList();
        Map<Long, Boolean> ratingMap = ratingRepository.findRatedMap(memberId, duoIds, matchIds);


        return myChatRoomMembers.stream()
                .filter(crm -> !crm.getMember().getId().equals(memberId)) // 나 자신 제외
                .map(duoCRM -> {
                    ChatRoom chatRoom = duoCRM.getChatRoom();
                    String matchId = chatRoom.getMatchId();
                    Member duo = duoCRM.getMember();

                    // (2) 이미 평점을 남겼는지 확인
                    boolean isRated = ratingMap.getOrDefault(duo.getId(), false);

                    // (3) DuoListResponse 생성
                    return new DuoResponse(
                            duoCRM.getId(),
                            duo.getId(),
                            duo.getProfileImageId(),
                            duo.getPlayerName(),
                            duo.getPlayerTag(),
                            matchId,
                            isRated
                    );
                })
                .toList();
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

