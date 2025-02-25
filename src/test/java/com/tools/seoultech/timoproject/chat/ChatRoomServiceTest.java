package com.tools.seoultech.timoproject.chat;

import com.tools.seoultech.timoproject.chat.model.ChatRoom;
import com.tools.seoultech.timoproject.chat.model.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomMemberRepository;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
import com.tools.seoultech.timoproject.chat.service.MessageService;
import com.tools.seoultech.timoproject.chat.service.impl.ChatRoomServiceImpl;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @Mock
    private MessageService messageService;

    @Spy
    Member member;

    @Spy
    ChatRoom chatRoom;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void init() {
        doReturn(1L).when(member).getId();
        System.out.println("member. getId : " + member.getId());
    }

    @Test
    void 채팅방_생성(){
        // Given
        when(chatRoomRepository.save(any(ChatRoom.class))).thenAnswer(invocation -> {
            ChatRoom savedChatRoom = invocation.getArgument(0);
            // spy 처리하여 getId() 호출 시 1L 반환하도록 설정
            ChatRoom spyRoom = spy(savedChatRoom);
            doReturn(1L).when(spyRoom).getId();
            return spyRoom;
        });

        // When
        ChatRoom result = chatRoomService.createChatRoom();

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(chatRoomRepository).save(any(ChatRoom.class));
    }

    @Test
    void 방안에_유저가_존재하는지_확인(){
        // Given
        doReturn(1L).when(chatRoom).getId();
        when(chatRoomMemberRepository.existsByMemberIdAndChatRoomId(1L, 1L)).thenReturn(true);

        // When
        chatRoomService.validateMemberInRoom(member, chatRoom);

        // Then
        verify(chatRoomMemberRepository).existsByMemberIdAndChatRoomId(1L, 1L);

    }

    @Test
    void 최근_메시지ID_반환(){
        // Given
        // ChatRoomMember 객체와 그 안의 마지막 확인 메시지(Message)를 Mock 처리합니다.
        Message lastMessage = mock(Message.class);
        doReturn(100L).when(lastMessage).getId();

        ChatRoomMember chatRoomMember = mock(ChatRoomMember.class);
        when(chatRoomMember.getLastCheckedMessage()).thenReturn(lastMessage);

        when(chatRoomMemberRepository.findByMemberIdAndChatRoomId(member.getId(), 1L))
                .thenReturn(Optional.of(chatRoomMember));

        // When
        Long recentMessageId = chatRoomService.getRecentMessageId(member.getId(), 1L);

        // Then
        assertNotNull(recentMessageId);
        assertEquals(100L, recentMessageId);
    }

    @Test
    void 방안에_존재하지_않은_유저(){
        // Given
        doReturn(1L).when(chatRoom).getId();
        when(chatRoomMemberRepository.existsByMemberIdAndChatRoomId(1L, 1L)).thenReturn(false);

        // When
        // Then
        try {
            chatRoomService.validateMemberInRoom(member, chatRoom);
        } catch (EntityNotFoundException e) {
            assertEquals("Member not found in ChatRoom with id: 1", e.getMessage());
        }
    }

    @Test
    void 존재하는_채팅방ID를_조회하면_채팅방이_반환된다() {
        // Given
        doReturn(1L).when(chatRoom).getId();
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));

        // When
        ChatRoom result = chatRoomService.getById(1L);
        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void 채팅방_입장시_유저정보가_저장되고_입장_메시지가_전송된다() {
        // Given
        doReturn(1L).when(chatRoom).getId();
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));

        // When
        chatRoomService.chatRoomJoin(1L, member);

        // Then
        verify(messageService).saveMessage(any(Message.class));
        verify(chatRoomMemberRepository).save(any(ChatRoomMember.class));
    }
}
