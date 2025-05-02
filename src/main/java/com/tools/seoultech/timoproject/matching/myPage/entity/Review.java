<<<<<<<< HEAD:src/main/java/com/tools/seoultech/timoproject/version2/matching/domain/myPage/entity/Reivew.java
package com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.OpponentAttitude;
import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.OpponentConversation;
import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.OpponentTalent;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
========
package com.tools.seoultech.timoproject.matching.myPage.entity;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.matching.myPage.entity.EnumType.OpponentAttitude;
import com.tools.seoultech.timoproject.matching.myPage.entity.EnumType.OpponentConversation;
import com.tools.seoultech.timoproject.matching.myPage.entity.EnumType.OpponentTalent;
import com.tools.seoultech.timoproject.memberAccount.domain.MemberAccount;
>>>>>>>> develop:src/main/java/com/tools/seoultech/timoproject/matching/myPage/entity/Review.java
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "member_account_id")
    // TODO: 듀오 같은 경우, Timo.GG 사용자가 아닌 비회원 사용자면 어떻게 리뷰를 저장할건지?
    private MemberAccount member;

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
