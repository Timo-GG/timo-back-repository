package com.tools.seoultech.timoproject.rating;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
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

    @Builder
    public Rating(BigDecimal score, Attitude attitude, Speech speech, Skill skill) {
        this.score = score;
        this.attitude = attitude;
        this.speech = speech;
        this.skill = skill;
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
