package com.tools.seoultech.timoproject.member.facade;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.member.dto.MemberInfoResponse;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.post.repository.CommentRepository;
import com.tools.seoultech.timoproject.post.repository.PostRepository;
import com.tools.seoultech.timoproject.rating.RatingRepository;
import com.tools.seoultech.timoproject.rating.RatingService;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberFacadeImpl implements MemberFacade {

    private final RatingService ratingService;
    private final MemberService memberService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;
    private final BasicAPIService riotService;

    @Override
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberService.getById(memberId);
        return buildMemberInfoResponse(member);
    }

    @Override
    public AccountDto.Response verifyPlayer(AccountDto.Request request) {
        try {
            return riotService.findUserAccount(request);
        } catch (Exception e) {
            log.error("Failed to verify player: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to verify player", e);
        }
    }

    @Override
    public boolean checkNickname(String nickname) {
        return memberService.checkNickname(nickname);
    }

    @Override
    public String createRandomNickname() {
        return memberService.randomCreateNickname();
    }

    @Override
    public MemberInfoResponse updateMemberInfo(Long memberId, UpdateMemberInfoRequest request) {
        // 1) 회원 정보 업데이트
        Member member = memberService.updateAdditionalInfo(
                memberId,
                request.nickname(),
                request.playerName(),
                request.playerTag()
        );
        // 2) 업데이트된 Member로부터 응답 DTO 생성
        return buildMemberInfoResponse(member);
    }

    @Override
    public Integer updateProfileImageId(Long memberId, Integer imageId) {
        return memberService.updateProfileImageId(memberId, imageId);
    }

    /**
     * 중복되는 통계 조회(평균 평점, 게시글/댓글/평가 수) 로직을 하나의 메서드로 추출
     */
    private MemberInfoResponse buildMemberInfoResponse(Member member) {
        BigDecimal ratingAverage = ratingService.getRatingAverage(member.getId());
        long postCount = postRepository.countByMemberId(member.getId());
        long commentCount = commentRepository.countByMemberId(member.getId());
        long ratingCount = ratingRepository.countByMemberId(member.getId());

        return MemberInfoResponse.of(member, postCount, commentCount, ratingCount, ratingAverage);
    }
}

