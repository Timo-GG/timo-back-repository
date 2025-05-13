package com.tools.seoultech.timoproject.review;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentAttitude;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentConversation;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentTalent;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.Member;

public record ReviewRequestDto(
        Long revieweeId,
        OpponentAttitude attitudeScore,
        OpponentConversation conversationScore,
        OpponentTalent talentScore,
        Integer evaluationScore,
        String memo
) {
    public Review toEntity(Member reviewer, Member reviewee) {
        return Review.builder()
                .reviewer(reviewer)
                .reviewee(reviewee)
                .attitude_score(attitudeScore)
                .conversation_score(conversationScore)
                .talent_score(talentScore)
                .evaluation_score(evaluationScore)
                .memo(memo)
                .build();
    }
}