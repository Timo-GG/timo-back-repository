package com.tools.seoultech.timoproject.socket.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.dto.ChatMessageDTO;
import com.tools.seoultech.timoproject.chat.dto.ChatSocketDTO;
import com.tools.seoultech.timoproject.chat.dto.ReadMessageRequest;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomMemberRepository;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketMessageService {

    private final ChatService chatService;
    private final ChatNotificationService notificationService;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public ChatMessageDTO processMessage(Long senderId, ChatMessageDTO data) {
        // 메시지 검증
        validateMessage(data);

        // 메시지 저장
        ChatMessageDTO chatMessage = ChatMessageDTO.builder()
                .roomId(data.roomId())
                .senderId(senderId)
                .content(data.content().trim())
                .build();

        ChatMessageDTO savedMessage = chatService.saveMessage(chatMessage);

        // 비동기 알림 전송 (협력)
        notificationService.sendNotificationAsync(data.roomId(), senderId, data.content());

        return savedMessage;
    }

    public void broadcastMessage(SocketIOServer server, ChatMessageDTO message, UUID senderSessionId) {
        String roomName = createRoomName(message.roomId());

        ChatSocketDTO<ChatMessageDTO> eventDTO = ChatSocketDTO.<ChatMessageDTO>builder()
                .eventType("receive_message")
                .roomId(message.roomId())
                .opponentId(message.senderId())
                .payload(message)
                .build();

        server.getRoomOperations(roomName).getClients().forEach(client -> {
            client.sendEvent("receive_message", eventDTO);
            log.debug("메시지 전송: clientId={}, sessionId={}", client.get("memberId"), client.getSessionId());
        });

    }

    public void markAsRead(Long memberId, ReadMessageRequest request) {
        log.info("읽음 처리 시작: memberId={}, roomId={}, messageId={}",
                memberId, request.roomId(), request.messageId());

        ChatRoomMember chatRoomMember = chatRoomMemberRepository
                .findByChatRoomIdAndMember_MemberId(request.roomId(), memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        log.info("읽음 처리 전 unreadCount: {}", chatRoomMember.getUnreadCount());

        chatRoomMember.resetUnreadCount();
        chatRoomMember.updateLastReadMessageId(request.messageId());
        chatRoomMemberRepository.save(chatRoomMember);

        log.info("읽음 처리 완료: unreadCount={}", chatRoomMember.getUnreadCount());
    }

    private void validateMessage(ChatMessageDTO data) {
        if (data == null || data.roomId() == null ||
                data.content() == null || data.content().trim().isEmpty()) {
            throw new IllegalArgumentException("잘못된 메시지 형식입니다");
        }

        if (data.content().length() > 1000) {
            throw new IllegalArgumentException("메시지가 너무 깁니다");
        }
    }

    private String createRoomName(Long roomId) {
        return "chat_" + roomId;
    }
}

