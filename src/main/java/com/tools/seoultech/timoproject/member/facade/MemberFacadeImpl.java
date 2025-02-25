package com.tools.seoultech.timoproject.member.facade;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.member.dto.MemberInfoResponse;
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
        BigDecimal ratingAverage = ratingService.getRatingAverage(member.getId());
        long postCount = postRepository.countByMemberId(member.getId());
        long commentCount = commentRepository.countByMemberId(member.getId());
        long ratingCount = ratingRepository.countByMemberId(member.getId());
        return MemberInfoResponse.of(member, postCount, commentCount, ratingCount, ratingAverage);
    }

    @Override
    public AccountDto.Response verifyPlayer(AccountDto.Request request) {
        try{
            AccountDto.Response response = riotService.findUserAccount(request);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkNickname(String nickname) {return memberService.checkNickname(nickname);}


    @Override
    public String createRandomNickname() {return memberService.randomCreateNickname();}
}
