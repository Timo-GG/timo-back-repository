package com.tools.seoultech.timoproject.chat;

import com.tools.seoultech.timoproject.auth.config.SecurityConfig;
import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomMemberRepository;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.global.config.TestSecurityConfig;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
// import org.springframework.transaction.annotation.Transactional;
// → @SpringBootTest에서 테스트 메서드 단위로 @Transactional을 쓸 수도 있음

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class
        )
)
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        // 테스트용 Member를 하나 만들어 DB에 저장
        // (실제로는 이미 DB에 존재할 수도 있음)
        Member member = Member.builder()
                .nickname("티모대위_12345")
                .build();
        testMember = memberRepository.save(member);
    }

    @Test
    @DisplayName("채팅방 생성 테스트")
    void createChatRoomTest() {
        // given
        String roomName = "testRoom";

        // when
        ChatRoom createdRoom = chatService.createChatRoom(roomName);

        // then
        assertThat(createdRoom).isNotNull();
        assertThat(createdRoom.getId()).isNotNull();
        assertThat(createdRoom.getChatRoomName()).isEqualTo(roomName);

        // DB에서 실제로 잘 저장됐는지 확인
        ChatRoom found = chatRoomRepository.findById(createdRoom.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getChatRoomName()).isEqualTo(roomName);
    }

    @Test
    @DisplayName("채팅방에 joinRoom 성공 테스트")
    void joinRoomTest() {
        // given
        ChatRoom createdRoom = chatService.createChatRoom("joinTestRoom");

        // when
        chatService.joinRoom(testMember.getId(), createdRoom.getId());

        // then
        // 실제 DB에서 ChatRoomMember가 생성됐는지 확인
        ChatRoomMember roomMember = chatRoomMemberRepository
                .findByChatRoomIdAndMemberId(createdRoom.getId(), testMember.getId())
                .orElse(null);
        assertThat(roomMember).isNotNull();
        assertThat(roomMember.getChatRoom().getId()).isEqualTo(createdRoom.getId());
        assertThat(roomMember.getMember().getId()).isEqualTo(testMember.getId());
    }

    @Test
    @DisplayName("이미 참여한 방에 joinRoom 시도하면 예외 발생")
    void joinRoomDuplicateTest() {
        // given
        ChatRoom createdRoom = chatService.createChatRoom("duplicateTestRoom");
        chatService.joinRoom(testMember.getId(), createdRoom.getId());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            chatService.joinRoom(testMember.getId(), createdRoom.getId());
        });
    }

    @Nested
    @DisplayName("추가 시나리오: 두 명이 한 방에 joinRoom")
    class MultiMemberJoinTest {
        private Member anotherMember;

        @BeforeEach
        void setUpAnotherMember() {
            // 추가 멤버 생성
            Member newMember = Member.builder()
                    .nickname("티모대위_12345")
                    .build();
            anotherMember = memberRepository.save(newMember);
        }

        @Test
        @DisplayName("두 명이 joinRoom 후, 방에 멤버가 2명인지 확인")
        void twoMembersJoinRoom() {
            // given
            ChatRoom room = chatService.createChatRoom("multiJoinRoom");

            // when
            chatService.joinRoom(testMember.getId(), room.getId());
            chatService.joinRoom(anotherMember.getId(), room.getId());

            // then
            List<ChatRoomMember> members = chatRoomMemberRepository.findByChatRoom(room);
            assertThat(members).hasSize(2);
        }
    }
}
