package com.tools.seoultech.timoproject.rating;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomMemberRepository;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.rating.dto.DuoResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class RatingServiceNPlusOneTest {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private SessionFactory sessionFactory;

    private Member mainUser;  // 테스트에서 사용할 메인 사용자 ID

    @BeforeEach
    void setUp() {
        // 1) SessionFactory 초기화 + Hibernate 통계 활성화
        sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        // 2) 테스트용 사용자/채팅방/채팅방멤버 생성
        //    예: mainUser가 10개 채팅방에 속해 있고, 각 채팅방에는 다른 듀오가 1명씩 존재
        mainUser = createMember("MainUser");

        for (int i = 0; i < 10; i++) {
            Member duo = createMember("Duo_" + i);

            ChatRoom chatRoom = ChatRoom.createRoom("MatchId_" + i);
            chatRoom = chatRoomRepository.save(chatRoom);

            ChatRoomMember crm1 = ChatRoomMember.createChatRoomMember(chatRoom, mainUser);
            ChatRoomMember crm2 = ChatRoomMember.createChatRoomMember(chatRoom, duo);
            chatRoomMemberRepository.save(crm1);
            chatRoomMemberRepository.save(crm2);
        }

        // 3) flush + clear로 1차 캐시 초기화
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("getDuoList N+1 테스트")
    void testNPlusOneInGetDuoList() {
        // 1) Hibernate 통계 설정
        Statistics statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();

        // 2) 실제 메서드 호출
        List<DuoResponse> duoList = ratingService.getDuoList(mainUser.getId());
        System.out.println("DuoList size = " + duoList.size());

        // 3) 쿼리 수 확인
        long queryCount = statistics.getPrepareStatementCount();
        System.out.println("N+1 test in getDuoList - Executed SQL query count: " + queryCount);

        // 기대치: 1) mainUser의 ChatRoomMember 목록 조회 쿼리
        //       2) 각 ChatRoom마다 멤버 조회 쿼리 (10개)
        //       3) 각 듀오마다 평점 여부(hasRated) 조회 쿼리 등
        // => 대략 1 + 10 + (추가) >= 11 이상을 기대
//        assertTrue(queryCount >= 11,
//                "N+1 query problem not detected, queryCount = " + queryCount);
        assertThat(queryCount).isLessThanOrEqualTo(2);

    }

    // ==============================
    // 헬퍼 메서드
    // ==============================
    private Member createMember(String name) {
        Member m = Member.builder()
                .nickname(name)
                .profileImageId(1)
                .build();
        // ... 기타 필드 세팅
        return memberRepository.save(m);
    }
    }