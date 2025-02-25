package com.tools.seoultech.timoproject.chat.facade.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.tools.seoultech.timoproject.chat.model.ChatRoom;
import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.chat.dto.MessageResponse;
import com.tools.seoultech.timoproject.chat.dto.PageResult;
import com.tools.seoultech.timoproject.chat.service.ChatRoomService;
import com.tools.seoultech.timoproject.chat.service.MessageService;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomFacadeImplTest {

    @InjectMocks
    private ChatRoomFacadeImpl chatRoomFacade;

    @Mock
    private ChatRoomService chatRoomService;

    @Mock
    private MessageService messageService;

    @Mock
    private MemberService memberService;

    @Mock
    private SocketIOClient socketIOClient;

    @Mock
    private Member member;
    @Mock
    private ChatRoom chatRoom;
    @Mock
    private Message message;

    @BeforeEach
    void setUp() {

    }

    @Test
    void sendTextMessage_성공적으로_메시지를_전송하고_DTO로_반환한다() {
        // Given
        when(memberService.getById(1L)).thenReturn(member);
        when(chatRoomService.getById(100L)).thenReturn(chatRoom);
        when(messageService.saveMessage(any(Message.class))).thenReturn(message);
        when(message.getId()).thenReturn(999L);
        when(message.getContent()).thenReturn("Hello!");

        // When
        MessageResponse response = chatRoomFacade.sendTextMessage(socketIOClient, 1L, 100L, "Hello!");

        // Then
        assertNotNull(response);
        assertEquals(999L, response.messageId());
        assertEquals("Hello!", response.content());
        verify(memberService, times(1)).getById(1L);
        verify(chatRoomService, times(1)).getById(100L);
        verify(chatRoomService, times(1)).validateMemberInRoom(member, chatRoom);
        verify(messageService, times(1)).saveMessage(any(Message.class));
    }

    @Test
    void receiveMessage_정상적으로_메시지를_받으면_예외없이_처리된다() {
        // Given
        when(memberService.getById(1L)).thenReturn(member);
        when(chatRoomService.getById(100L)).thenReturn(chatRoom);
        when(messageService.getById(999L)).thenReturn(message);

        // When
        chatRoomFacade.receiveMessage(socketIOClient, 1L, 100L, 999L);

        // Then
        verify(memberService, times(1)).getById(1L);
        verify(chatRoomService, times(1)).getById(100L);
        verify(chatRoomService, times(1)).validateMemberInRoom(member, chatRoom);
        verify(messageService, times(1)).getById(999L);
        // todo : update last checked message - 아직 구현 X
    }

    @Test
    void getMessages_메시지리스트와_hasNext정보를_반환한다() {
        // Given
        when(memberService.getById(1L)).thenReturn(member);
        when(chatRoomService.getById(100L)).thenReturn(chatRoom);
        when(chatRoom.getId()).thenReturn(100L);


        // 페이징 Slice<Message> 준비
        List<Message> content = List.of(message);
        Slice<Message> slice = new PageImpl<>(content, PageRequest.of(0, 10), content.size()); // total = content.size()

        when(chatRoomService.getBeforeMessagesByPaging(100L, 1L, 10)).thenReturn(slice);

        // When
        PageResult<MessageResponse> result = chatRoomFacade.getMessages(1L, 100L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.values().size());
        assertEquals(message.getId(), result.values().get(0).messageId());
        verify(memberService, times(1)).getById(1L);
        verify(chatRoomService, times(1)).getById(100L);
        verify(chatRoomService, times(1)).validateMemberInRoom(member, chatRoom);
    }

    @Test
    void getRecentMessageId_유저가_읽은_메시지ID를_반환한다() {
        // Given
        when(memberService.getById(1L)).thenReturn(member);
        when(chatRoomService.getById(100L)).thenReturn(chatRoom);
        when(member.getId()).thenReturn(1L);
        when(chatRoom.getId()).thenReturn(100L);
        when(chatRoomService.getRecentMessageId(1L, 100L)).thenReturn(555L);

        // When
        Long recentMessageId = chatRoomFacade.getRecentMessageId(1L, 100L);

        // Then
        assertEquals(555L, recentMessageId);
        verify(memberService, times(1)).getById(1L);
        verify(chatRoomService, times(1)).getById(100L);
        verify(chatRoomService, times(1)).validateMemberInRoom(member, chatRoom);
        verify(chatRoomService, times(1)).getRecentMessageId(1L, 100L);
    }
}
