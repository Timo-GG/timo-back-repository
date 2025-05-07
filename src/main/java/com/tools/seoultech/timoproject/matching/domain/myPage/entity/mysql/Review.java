package com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentAttitude;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentConversation;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.OpponentTalent;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
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
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private MemberAccount reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private MemberAccount reviewee;

    @Column(nullable = false)
    private OpponentAttitude attitude_score;

    @Column(nullable = false)
    private OpponentConversation conversation_score;

    @Column(nullable = false)
    private OpponentTalent talent_score;

    @Column(nullable = false)
    private Integer evaluation_score;

    @Column(nullable = false)
    private String memo;
}
