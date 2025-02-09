package com.tools.seoultech.timoproject.rating;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RatingServiceTest {

    Rating rating;

    @Autowired
    RatingRepository ratingRepository;

    @BeforeEach
    void setUp() {
        rating = Rating.builder()
                .score(BigDecimal.valueOf(4.5))
                .attitude(Rating.Attitude.GOOD)
                .speech(Rating.Speech.MANNERS)
                .skill(Rating.Skill.LEARNING)
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