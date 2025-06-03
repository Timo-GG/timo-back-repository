package com.tools.seoultech.timoproject.review;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentAttitude;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentConversation;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentTalent;

public record ReviewResponseDto(
        OpponentAttitude attitudeScore,
        OpponentConversation conversationScore,
        OpponentTalent talentScore,
        Integer evaluationScore,
        String memo
) {
    public static ReviewResponseDto fromEntity(Review review) {
        return new ReviewResponseDto(
                review.getAttitude_score(),
                review.getConversation_score(),
                review.getTalent_score(),
                review.getEvaluation_score(),
                review.getMemo()
        );
    }
}
