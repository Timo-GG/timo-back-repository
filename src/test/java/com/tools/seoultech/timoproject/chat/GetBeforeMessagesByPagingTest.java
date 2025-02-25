package com.tools.seoultech.timoproject.chat;

import com.tools.seoultech.timoproject.chat.model.ChatRoom;
import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.chat.model.MessageType;
import com.tools.seoultech.timoproject.chat.repository.MessageRepository;
import com.tools.seoultech.timoproject.chat.service.impl.ChatRoomServiceImpl;
import com.tools.seoultech.timoproject.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("메시지 조회 테스트")
class GetBeforeMessagesByPagingTest {

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    @Mock
    private MessageRepository messageRepository;

    // 스파이로 선언할 엔티티들
    @Spy
    private ChatRoom chatRoom;

    @Spy
    private Member member;

    // 실제 메시지 목록
    private List<Message> messages = new ArrayList<>();

    @BeforeEach
    void setup() {
        chatRoom = spy(ChatRoom.newInstance());
        doReturn(1L).when(chatRoom).getId();

        member = spy(Member.builder()
                .email("dummy@example.com")
                .username("dummyUser")
                .nickname("테스트닉네임")
                .build());
        doReturn(1L).when(member).getId();

        // 메시지 1: ID = 5
        Message message1 = spy(Message.builder()
                .chatRoom(chatRoom)
                .member(member)
                .messageType(MessageType.TEXT)
                .content("Message #5")
                .username(member.getUsername())
                .room("myRoom")
                .build());
        doReturn(5L).when(message1).getId();

        // 메시지 2: ID = 4
        Message message2 = spy(Message.builder()
                .chatRoom(chatRoom)
                .member(member)
                .messageType(MessageType.TEXT)
                .content("Message #4")
                .username(member.getUsername())
                .room("myRoom")
                .build());
        doReturn(4L).when(message2).getId();

        // 메시지 3: ID = 2
        Message message3 = spy(Message.builder()
                .chatRoom(chatRoom)
                .member(member)
                .messageType(MessageType.TEXT)
                .content("Message #2")
                .username(member.getUsername())
                .room("myRoom")
                .build());
        doReturn(2L).when(message3).getId();

        messages.add(message1);
        messages.add(message2);
        messages.add(message3);
    }

    @Test
    void 메시지ID가_null이면_최신메시지부터_페이징조회된다() {
        // Given
        Slice<Message> dummySlice = new SliceImpl<>(
                messages,                // 더미 메시지 목록
                PageRequest.of(0, 10),   // (page=0, size=10)
                false                    // hasNext
        );

        when(messageRepository.findByChatRoomIdOrderByIdDesc(chatRoom.getId(), PageRequest.of(0, 10)))
                .thenReturn(dummySlice);

        // When
        Slice<Message> result = chatRoomService.getBeforeMessagesByPaging(chatRoom.getId(), null, 10);

        // Then
        assertEquals(dummySlice, result);
        verify(messageRepository, times(1))
                .findByChatRoomIdOrderByIdDesc(chatRoom.getId(), PageRequest.of(0, 10));
    }

    @Test
    void 메시지ID가_null이_아니면_해당ID보다_이전의_메시지들을_페이징조회한다() {
        // Given
        long messageId = 3L;
        Slice<Message> dummySlice = new SliceImpl<>(messages, PageRequest.of(0, 10), false);
        when(messageRepository.findByRoomIdLessThanIdOrderByIdDesc(chatRoom.getId(), messageId, PageRequest.of(0, 10)))
                .thenReturn(dummySlice);

        // When
        Slice<Message> result = chatRoomService.getBeforeMessagesByPaging(chatRoom.getId(), messageId, 10);

        // Then
        assertEquals(dummySlice, result);
        verify(messageRepository, times(1))
                .findByRoomIdLessThanIdOrderByIdDesc(chatRoom.getId(), messageId, PageRequest.of(0, 10));
    }

    @Test
    void messageId보다_작은_메시지들만_페이징조회된다() {
        // Given
        long messageId = 4L; // 이전 메시지 기준
        // 메시지 ID = 5, 4, 2
        // 여기서 ID < 4 이면 => 2번 메시지만 해당

        // 메시지 중에서 ID가 2인 것만 포함된 Slice를 준비
        List<Message> olderMessages = messages.stream()
                .filter(msg -> msg.getId() < messageId) // ID < 4
                .toList(); // => ID=2 메시지만

        // SliceImpl (hasNext = false 라고 가정)
        Slice<Message> dummySlice = new SliceImpl<>(olderMessages, PageRequest.of(0, 10), false);

        when(messageRepository.findByRoomIdLessThanIdOrderByIdDesc(chatRoom.getId(), messageId, PageRequest.of(0, 10)))
                .thenReturn(dummySlice);

        // When
        Slice<Message> result = chatRoomService.getBeforeMessagesByPaging(chatRoom.getId(), messageId, 10);

        // Then
        // 1) 레포지토리 호출 검증
        verify(messageRepository, times(1))
                .findByRoomIdLessThanIdOrderByIdDesc(chatRoom.getId(), messageId, PageRequest.of(0, 10));

        // 2) 반환된 메시지 검증
        assertEquals(1, result.getContent().size());
        assertEquals(2L, result.getContent().get(0).getId());
    }

}
