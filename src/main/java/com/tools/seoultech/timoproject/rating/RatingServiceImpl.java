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
        // (1) 해당 member가 참여했던 모든 ChatRoomMember 엔티티 조회
        List<ChatRoomMember> myChatRoomMembers = chatRoomMemberRepository.findByMemberId(memberId);

        // (2) 각 ChatRoomMember로부터 '같은 채팅방에 있는 다른 멤버(듀오)'를 찾아서
        //     DuoListResponse를 만들어준다.
        return myChatRoomMembers.stream()
                .map(myCRM -> {
                    ChatRoom chatRoom = myCRM.getChatRoom();
                    String matchId = chatRoom.getMatchId();

                    // 채팅방에 속한 전체 멤버들
                    List<ChatRoomMember> allMembersInThisRoom = (List<ChatRoomMember>) chatRoomMemberRepository.findByChatRoomId(chatRoom.getId());
                    // 나 자신(memberId)이 아닌 사람(=듀오)을 찾는다
                    ChatRoomMember duoCRM = allMembersInThisRoom.stream()
                            .filter(crm -> !crm.getMember().getId().equals(memberId))
                            .findFirst()
                            .orElse(null);
                    // 1:1 채팅방 구조라면 .get()으로 가져와도 되지만, 널 체크는 습관적으로 해주는 편이 안전

                    if (duoCRM == null) {
                        throw new BusinessException(ErrorCode.NOT_FOUND_DUO_EXCEPTION);
                    }

                    Member duo = duoCRM.getMember();
                    // (3) 이미 평점을 남겼는지 확인
                    boolean isRated = hasRated(memberId, duo.getId(), matchId);

                    // (4) DuoListResponse 생성
                    return new DuoResponse(
                            myCRM.getId(),         // 혹은 chatRoom.getId() 등 원하는 식별자
                            duo.getId(),
                            duo.getProfileImageId(), // 예시. 실제로는 duo의 프로필 필드에 맞춰서
                            duo.getPlayerName(),   // 예시
                            duo.getPlayerTag(),    // 예시
                            matchId,
                            isRated
                    );
                })
                // null이 들어올 수도 있으니 필터링
                .filter(Objects::nonNull)
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

