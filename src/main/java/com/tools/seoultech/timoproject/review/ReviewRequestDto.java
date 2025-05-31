package com.tools.seoultech.timoproject.review;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentAttitude;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentConversation;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentTalent;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import com.tools.seoultech.timoproject.member.domain.entity.Member;

public record ReviewRequestDto(
        Long revieweeId,
        Long mypageId,
        OpponentAttitude attitudeScore,
        OpponentConversation conversationScore,
        OpponentTalent talentScore,
        Integer evaluationScore,
        String memo
) {
    public Review toEntity(Member reviewer, Member reviewee, MyPage myPage) {
        return Review.builder()
                .reviewer(reviewer)
                .reviewee(reviewee)
                .myPage(myPage)
                .attitude_score(attitudeScore)
                .conversation_score(conversationScore)
                .talent_score(talentScore)
                .evaluation_score(evaluationScore)
                .memo(memo)
                .build();
    }
}