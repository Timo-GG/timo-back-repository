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
}
