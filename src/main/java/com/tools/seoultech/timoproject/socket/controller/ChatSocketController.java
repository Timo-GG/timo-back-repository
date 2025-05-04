package com.tools.seoultech.timoproject.socket.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.dto.*;
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
        Long roomId = data.roomId();
        String roomName = "chat_" + roomId;

        // 클라이언트가 보낸 DTO를 기반으로 새 메시지 DTO 생성
        ChatMessageDTO chatMessage = ChatMessageDTO.builder()
                .roomId(roomId)
                .senderId(senderId)
                .content(data.content())
                .build();

        // DB 저장 후, 생성된 메시지 DTO 반환
        ChatMessageDTO savedChatMessage = chatService.saveMessage(chatMessage);

        // 공통 DTO에 필요한 필드들을 세팅하여 메시지 이벤트 생성
        ChatSocketDTO<ChatMessageDTO> eventDTO = ChatSocketDTO.<ChatMessageDTO>builder()
                .eventType("receive_message")
                .roomId(roomId)
                .opponentId(senderId)
                .payload(savedChatMessage)
                .build();

        // 같은 방에 접속한 클라이언트들에게 메시지 전송 (보낸 클라이언트 제외)
        senderClient.getNamespace().getRoomOperations(roomName).getClients().forEach(client -> {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("receive_message", eventDTO);
            }
        });
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
        Long roomId = request.roomId();
        String roomName = "chat_" + roomId;

        log.info("[leave_room] memberId={}, roomName={}", memberId, roomName);

        // 1) 소켓 세션에서 방 제거
        client.leaveRoom(roomName);

        // 2) DB에서 ChatRoomMember 삭제 또는 상태 변경
//        chatService.leaveRoom(memberId, roomId);

        // 3) 시스템 메시지를 위한 공통 DTO 생성
        ChatSocketDTO<String> systemEventDTO = ChatSocketDTO.<String>builder()
                .eventType("leave")
                .roomId(roomId)
                .opponentId(memberId)
                .payload("Member " + memberId + " left the room: " + roomName)
                .build();

        // 같은 방에 접속 중인 다른 클라이언트들에게 시스템 메시지 전송
        server.getRoomOperations(roomName).sendEvent("leave", systemEventDTO);

        log.info("[leave_room] {}번 회원이 {} 방에서 나갔습니다.", memberId, roomName);
    }

    @SocketMapping(endpoint = "join_room", requestCls = JoinRoomRequest.class)
    public void handleJoinRoom(SocketIOClient client, SocketIOServer server, JoinRoomRequest request) {
        Long memberId = client.get("memberId");
        Long roomId = request.roomId();
        String roomName = "chat_" + roomId;

        client.joinRoom(roomName);
        chatService.joinRoom(memberId, roomId);
        log.info("[join_room] memberId={} joined room={}", memberId, roomName);
    }
}
