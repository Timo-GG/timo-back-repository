package com.tools.seoultech.timoproject.rating;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RatingServiceTest {

    Rating rating;
    Member member;

    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();

        member = Member.builder()
                .email("asdf123@gmail.com")
                .username("test")
                .build();
        memberRepository.save(member);

        rating = Rating.builder()
                .score(BigDecimal.valueOf(4.5))
                .attitude(Rating.Attitude.GOOD)
                .speech(Rating.Speech.MANNERS)
                .skill(Rating.Skill.LEARNING)
                .member(member)
                .build();

    }

    @Test
    void saveRating() {
        ratingRepository.save(rating);
        Assertions.assertThat(ratingRepository.count()).isGreaterThan(0);
    }

    @Test
    void deleteRating() {
        ratingRepository.save(rating);
        ratingRepository.deleteById(rating.getId());
        Assertions.assertThat(ratingRepository.count()).isEqualTo(0);
    }

    @Test
    void getRating() {
        ratingRepository.save(rating);
        Rating rating1 = ratingRepository.findById(rating.getId()).orElse(null);
        Assertions.assertThat(rating1).isNotNull();
    }
}