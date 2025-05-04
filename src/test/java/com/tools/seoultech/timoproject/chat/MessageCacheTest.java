package com.tools.seoultech.timoproject.chat;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.dto.ChatMessageDTO;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MessageCacheTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private Long testRoomId = 1L;

    private Member mainUser;

    @BeforeEach
    void setUp() {

        mainUser = createMember("MainUser");
        Member otherUser = createMember("OtherUser");  // DB에 저장 → 예: id=2
        ChatRoom chatRoom = ChatRoom.createRoom("TestRoom");
        chatRoom = chatRoomRepository.save(chatRoom);
        testRoomId = chatRoom.getId();
        // 테스트용 채팅방, 멤버 세팅
         chatService.createChatRoomForMatch("testPerf", mainUser.getId(), otherUser.getId());

        // 테스트 메시지 추가
        for (int i = 0; i < 1000; i++) {
            ChatMessageDTO dto = new ChatMessageDTO(null, testRoomId, mainUser.getId(), "Hello " + i);
            chatService.saveMessageWithCache(dto);
        }
    }


    @Test
    @DisplayName("1) 캐시 미사용 - 메시지 1000개 전송 성능")
    void testSendMessageWithoutCache() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            ChatMessageDTO dto = new ChatMessageDTO(null, testRoomId, 1L, "Hello " + i);
            chatService.saveMessage(dto);  // 즉시 DB 저장
        }

        long end = System.currentTimeMillis();
        System.out.println("캐시 미사용: 1000개 메시지 전송 소요 = " + (end - start) + " ms");
    }

    @Test
    @DisplayName("2) 캐시 사용 - 메시지 1000개 전송 성능")
    void testSendMessageWithCache() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            ChatMessageDTO dto = new ChatMessageDTO(null, testRoomId, 1L, "Hello " + i);
            chatService.saveMessageWithCache(dto);  // 캐시에만 저장
        }

        long end = System.currentTimeMillis();
        System.out.println("캐시 사용: 1000개 메시지 전송 소요 = " + (end - start) + " ms");
    }

    // 메시지 조회도 비교


    @Test
    @DisplayName("1) 캐시 미사용 - 메시지 조회 성능")
    void testGetMessagesWithoutCache() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) { // 10번 조회 반복
            List<ChatMessageDTO> messages = chatService.getMessages(testRoomId, i, 100);
            assert !messages.isEmpty();
        }

        long end = System.currentTimeMillis();
        System.out.println("캐시 미사용: 10회 메시지 조회 소요 = " + (end - start) + " ms");
    }

    @Test
    @DisplayName("2) 캐시 사용 - 메시지 조회 성능")
    void testGetMessagesWithCache() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) { // 10번 조회 반복
            List<ChatMessageDTO> messages = chatService.getMessagesWithCache(testRoomId, i, 100);
            assert !messages.isEmpty();
        }

        long end = System.currentTimeMillis();
        System.out.println("캐시 사용: 10회 메시지 조회 소요 = " + (end - start) + " ms");
    }

    private Member createMember(String name) {
        Member m = Member.builder()
                .nickname(name)
                .profileImageId(1)
                .build();
        return memberRepository.save(m);
    }
}