package com.tools.seoultech.timoproject.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.dto.ChatMessageDTO;
import com.tools.seoultech.timoproject.chat.dto.ReadMessageRequest;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomMemberRepository;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.global.annotation.SocketController;
import com.tools.seoultech.timoproject.global.annotation.SocketMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SocketController
@Slf4j
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatService chatService;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @SocketMapping(endpoint = "send_message", requestCls = ChatMessageDTO.class)
    public void handleSendMessage(SocketIOClient senderClient, SocketIOServer server,
                                  ChatMessageDTO data) {
        Long senderId = senderClient.get("memberId"); // 서버 세션에서 memberId 조회
        String room = data.room();

        // 1) 클라이언트가 보낸 DTO를 기반으로 새 메시지 DTO 생성 (id는 아직 없음)
        ChatMessageDTO chatMessage = ChatMessageDTO.builder()
                .room(room)
                .senderId(senderId)
                .content(data.content())
                .build();

        // 2) DB 저장 → DB에서 생성된 PK를 포함한 DTO를 반환
        ChatMessageDTO savedChatMessage = chatService.saveMessage(chatMessage);

        // 3) 같은 room에 접속한 클라이언트들에게 메시지 전송
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                // DB에서 PK까지 세팅된 savedChatMessage를 전송
                client.sendEvent("receive_message", savedChatMessage);
            }
        }
    }

    @SocketMapping(endpoint = "read_message", requestCls = ReadMessageRequest.class)
    public void handleReadMessage(SocketIOClient client,
                                  SocketIOServer server,
                                  ReadMessageRequest request) {
        Long memberId = client.get("memberId");

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(request.chatRoomId(), memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        chatRoomMember.resetUnreadCount();
        chatRoomMember.updateLastReadMessageId(request.messageId());
        chatRoomMemberRepository.save(chatRoomMember);


    }

}
