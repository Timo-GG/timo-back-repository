package com.tools.seoultech.timoproject.member.dto;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.rating.Rating;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class MemberRatingTest {

    Member member;
    Member duo;
    Rating rating;
    Rating rating2;

    @BeforeEach
    void init(){
        member = Member.builder()
                .email("asdf@gmail.com")
                .build();

        duo = Member.builder()
                .email("asdf_duo@gmail.com")
                .build();

        rating = Rating.builder()
                .score(BigDecimal.valueOf(4.5))
                .attitude(Rating.Attitude.GOOD)
                .speech(Rating.Speech.MANNERS)
                .skill(Rating.Skill.LEARNING)
                .member(member)
                .duo(duo)
                .build();

        rating2 = Rating.builder()
                .score(BigDecimal.valueOf(2.5))
                .attitude(Rating.Attitude.BAD)
                .speech(Rating.Speech.AGGRESSIVE)
                .skill(Rating.Skill.NORMAL)
                .member(member)
                .duo(duo)
                .build();

        member.linkRating(rating);
        member.linkRating(rating2);
    }

    @Test
    void linkMemberRating(){
        Assertions.assertThat(rating.getMember().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void getRatingScore(){
        Assertions.assertThat(rating.getScore()).isEqualTo(member.getRatings().get(0).getScore());
        Assertions.assertThat(rating2.getScore()).isEqualTo(member.getRatings().get(1).getScore());
    }

}
