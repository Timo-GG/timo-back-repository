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

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class MessageCacheTest2 {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private Long testRoomId;

    private Member mainUser;

    @BeforeEach
    void setUp() {
        mainUser = createMember("MainUser");
        Member otherUser = createMember("OtherUser");

        ChatRoom chatRoom = ChatRoom.createRoom("TestRoom");
        chatRoom = chatRoomRepository.save(chatRoom);
        testRoomId = chatRoom.getId();

        chatService.createChatRoomForMatch("testPerf", mainUser.getId(), otherUser.getId());

    }

    @Test
    @DisplayName("1) 캐시 미사용 - 메시지 조회 성능 (DB 조회)")
    void testGetMessagesWithoutCache() {

        // ✅ 테스트 메시지 추가 후 캐시 플러시
        for (int i = 0; i < 1000; i++) {
            ChatMessageDTO dto = new ChatMessageDTO(null, testRoomId, mainUser.getId(), "Hello " + i);
            chatService.saveMessageWithCache(dto);
        }
        long start = System.currentTimeMillis(); // 시작 시간 측정

        for (int i = 0; i < 10; i++) { // 10번 조회 반복
            List<ChatMessageDTO> messages = chatService.getMessages(testRoomId, i, 100);
            assertFalse(messages.isEmpty(), "DB에서 메시지를 찾을 수 없습니다.");
        }

        long end = System.currentTimeMillis(); // 종료 시간 측정
        System.out.println("🚀 캐시 미사용: 10회 메시지 조회 소요 = " + (end - start) + " ms");
    }

    @Test
    @DisplayName("2) 캐시 사용 - 메시지 조회 성능 (캐시 조회)")
    void testGetMessagesWithCache() {
        // ✅ 테스트 메시지 추가 후 캐시 플러시
        for (int i = 0; i < 1000; i++) {
            ChatMessageDTO dto = new ChatMessageDTO(null, testRoomId, mainUser.getId(), "Hello " + i);
            chatService.saveMessageWithCache(dto);
        }

        // ✅ 캐시 Flush 수행 (DB에 반영)
        chatService.scheduledFlush();

        long start = System.currentTimeMillis(); // 시작 시간 측정

        for (int i = 0; i < 10; i++) { // 10번 조회 반복
            List<ChatMessageDTO> messages = chatService.getMessagesWithCache(testRoomId, i, 100);
            assertFalse(messages.isEmpty(), "캐시에서 메시지를 찾을 수 없습니다.");
        }

        long end = System.currentTimeMillis(); // 종료 시간 측정
        System.out.println("⚡ 캐시 사용: 10회 메시지 조회 소요 = " + (end - start) + " ms");
    }

    private Member createMember(String name) {
        Member m = Member.builder()
                .nickname(name)
                .profileImageId(1)
                .build();
        return memberRepository.save(m);
    }
}
