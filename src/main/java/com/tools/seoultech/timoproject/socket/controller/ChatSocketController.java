package com.tools.seoultech.timoproject.socket.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.dto.ChatMessageDTO;
import com.tools.seoultech.timoproject.chat.dto.LeaveRoomRequest;
import com.tools.seoultech.timoproject.chat.dto.ReadMessageRequest;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomMemberRepository;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
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
    private final ChatRoomRepository chatRoomRepository;

    @SocketMapping(endpoint = "send_message", requestCls = ChatMessageDTO.class)
    public void handleSendMessage(SocketIOClient senderClient, SocketIOServer server,
                                  ChatMessageDTO data) {
        Long senderId = senderClient.get("memberId"); // 서버 세션에서 memberId 조회
        Long roomId = data.roomId();
        String roomName = "chat_"+roomId;

        // 1) 클라이언트가 보낸 DTO를 기반으로 새 메시지 DTO 생성 (id는 아직 없음)
        ChatMessageDTO chatMessage = ChatMessageDTO.builder()
                .roomId(roomId)
                .senderId(senderId)
                .content(data.content())
                .build();

        // 2) DB 저장 → DB에서 생성된 PK를 포함한 DTO를 반환
        ChatMessageDTO savedChatMessage = chatService.saveMessage(chatMessage);

        // 3) 같은 room에 접속한 클라이언트들에게 메시지 전송
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(roomName).getClients()) {
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

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(request.roomId(), memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        chatRoomMember.resetUnreadCount();
        chatRoomMember.updateLastReadMessageId(request.messageId());
        chatRoomMemberRepository.save(chatRoomMember);

    }

    @SocketMapping(endpoint = "leave_room", requestCls = LeaveRoomRequest.class)
    public void handleLeaveRoom(SocketIOClient client,
                                SocketIOServer server,
                                LeaveRoomRequest request) {
        Long memberId = client.get("memberId");
        String roomName = "chat_"+request.roomId();

        log.info("[leave_room] memberId={}, roomName={}", memberId, roomName);

        // 1) 소켓에서 방 제거
        client.leaveRoom(roomName);

        // 2) DB에서 ChatRoomMember 삭제 (또는 상태 변경)
        chatService.leaveRoom(memberId, request.roomId());

        // 3) (옵션) 다른 사용자에게 시스템 메시지 브로드캐스트
        String leaveMsg = "User " + memberId + " left the room: " + roomName;
        server.getRoomOperations(roomName).sendEvent("system_message", leaveMsg);

        log.info("[leave_room] {}번 회원이 {} 방에서 나갔습니다.", memberId, roomName);
    }


}
