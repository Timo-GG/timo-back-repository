package com.tools.seoultech.timoproject.review;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentAttitude;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentConversation;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentTalent;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import com.tools.seoultech.timoproject.member.domain.entity.Member;

public record ReviewRequestDto(
        OpponentAttitude attitudeScore,
        OpponentConversation conversationScore,
        OpponentTalent talentScore,
        Integer evaluationScore,
        String memo
) {
    public Review toEntity() {
        return Review.create(attitudeScore, conversationScore, talentScore,
                evaluationScore, memo);
    }
}