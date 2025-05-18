package com.tools.seoultech.timoproject.review;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentAttitude;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentConversation;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentTalent;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private Member reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id")
    private Member reviewee;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpponentAttitude attitude_score;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpponentConversation conversation_score;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpponentTalent talent_score;

    @Column(nullable = false)
    private Integer evaluation_score;

    @Column(nullable = false)
    private String memo;
}
