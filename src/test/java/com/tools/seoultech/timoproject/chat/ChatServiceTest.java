package com.tools.seoultech.timoproject.chat;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.Message;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
import com.tools.seoultech.timoproject.chat.repository.MessageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ChatServiceTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageRepository messageRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        // SessionFactory 초기화
        sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        // 기존 데이터 클리어 (필요하면)
        // chatRoomRepository.deleteAll();
        // messageRepository.deleteAll();

        // 테스트를 위해 30개의 채팅방을 각각 생성하고, 각 채팅방에 하나의 메시지를 저장합니다.
        for (int i = 0; i < 30; i++) {
            ChatRoom chatRoom = ChatRoom.createRoom("Room_" + i);
            chatRoom = chatRoomRepository.save(chatRoom);
            Message message = Message.createMessage(chatRoom, (long) i, "Message for room " + i);
            messageRepository.save(message);
        }
        // Spring이 관리하는 EntityManager를 flush 및 clear 하여 1차 캐시를 제거합니다.
        em.flush();
        em.clear();
    }

    @Test
    public void testNPlusOneIssue() {
        // Hibernate 통계 활성화
        Statistics statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();

        // 모든 메시지 조회 (각 메시지는 서로 다른 채팅방에 속함)
        List<Message> messages = messageRepository.findAll();

        // 1차 캐시 비우기 -> Lazy 로딩이 실제로 동작하게 만듦
        // 각 메시지의 채팅방 정보를 접근합니다.
        // 각 메시지마다 lazy 로딩으로 채팅방 정보를 불러오므로, 추가 쿼리가 발생해야 합니다.
        for (Message m : messages) {
            Hibernate.initialize(m.getChatRoom()); // Lazy 로딩 강제 초기화
            Long id = m.getChatRoom().getId();
            System.out.println("ChatRoom ID: " + id);  // 실제로 사용해야 Lazy 로딩 실행됨

        }

        long queryCount = statistics.getPrepareStatementCount();
        System.out.println("N+1 test - Executed SQL query count: " + queryCount);

        // 첫번째 쿼리: 메시지 조회 쿼리
        // 이후, 각 메시지에 대해 lazy 로딩되는 채팅방 조회 쿼리 (총 30개)가 발생하면,
        // 예상 쿼리 수는 대략 31개 이상이어야 합니다.
        assertTrue(queryCount >= 31, "N+1 query problem not detected, query count: " + queryCount);
    }
}
