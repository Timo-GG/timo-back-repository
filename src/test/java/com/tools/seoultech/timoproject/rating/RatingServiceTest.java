package com.tools.seoultech.timoproject.rating;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.rating.dto.RatingTotalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "/test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class RatingServiceTest {

    Member member;  // 평점을 받는 회원 (예: id=1)
    Member duo;     // 평점을 준 회원 (예: id=2)
    Rating rating;
    Rating rating2;

    @Autowired
    RatingService ratingService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private RatingRepository ratingRepository;

    @BeforeEach
    void init(){
        // test.sql 에서 이미 회원 데이터가 삽입되었으므로 id 값으로 조회
        member = memberRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Member not found"));
        duo = memberRepository.findById(2L)
                .orElseThrow(() -> new IllegalStateException("Duo not found"));

        rating = ratingRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Rating not found"));

        rating2 = ratingRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Rating not found"));

    }

    @Test
    void getMemberRatings() {
        RatingTotalResponse ratings = ratingService.getRatings(member.getId());
        assertEquals(2, ratings.ratings().size());
    }
}
