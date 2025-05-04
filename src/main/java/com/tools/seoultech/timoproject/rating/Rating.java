package com.tools.seoultech.timoproject.rating;

import com.tools.seoultech.timoproject.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "duo_id", "matchId"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal score;

    @Enumerated(value = EnumType.STRING)
    private Attitude attitude;

    @Enumerated(value = EnumType.STRING)
    private Speech speech;

    @Enumerated(value = EnumType.STRING)
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duo_id", nullable = false)
    private Member duo;

    @Column(nullable = false)
    private String matchId;

    @Builder
    public Rating(BigDecimal score, Attitude attitude, Speech speech, Skill skill, Member member, Member duo, String matchId) {
        this.score = score;
        this.attitude = attitude;
        this.speech = speech;
        this.skill = skill;
        this.member = member;
        this.duo = duo;
        this.matchId = matchId;
    }

    // score 평균 계산
    public static BigDecimal calculateAverageRatings(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return BigDecimal.ZERO; // 또는 적절한 기본값 처리
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (Rating rating : ratings) {
            sum = sum.add(rating.getScore());
        }
        return sum.divide(BigDecimal.valueOf(ratings.size()), 2, BigDecimal.ROUND_HALF_UP);
    }

    public void linkMember(Member member) {
        this.member = member;
    }

    public enum Attitude {
        GOOD,
        TRY_HARD,
        BAD
    }

    public enum Speech {
        MANNERS, // 매너있는
        ORDINARY, // 평범한
        AGGRESSIVE // 공격적인
    }

    public enum Skill {
        LEARNING, // 한 수 배우다
        NORMAL, // 무난한
        TROLL // 고의 트롤

    }
}
