package com.tools.seoultech.timoproject.review;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentAttitude;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentConversation;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentTalent;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private OpponentAttitude attitude_score;

    private OpponentConversation conversation_score;

    private OpponentTalent talent_score;

    private Integer evaluation_score;

    private String memo;

    private LocalDateTime createdAt;

    public static Review create(OpponentAttitude attitudeScore,
                                OpponentConversation conversationScore,
                                OpponentTalent talentScore,
                                Integer evaluationScore,
                                String memo) {
        return Review.builder()
                .attitude_score(attitudeScore)
                .conversation_score(conversationScore)
                .talent_score(talentScore)
                .evaluation_score(evaluationScore)
                .memo(memo)
                .createdAt(LocalDateTime.now())  // 자동으로 현재 시간 설정
                .build();
    }
}
