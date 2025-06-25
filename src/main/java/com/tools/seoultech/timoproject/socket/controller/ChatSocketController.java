package com.tools.seoultech.timoproject.socket.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.chat.dto.*;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.global.annotation.SocketController;
import com.tools.seoultech.timoproject.global.annotation.SocketMapping;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.socket.service.SocketAuthenticationService;
import com.tools.seoultech.timoproject.socket.service.SocketMessageService;
import com.tools.seoultech.timoproject.socket.service.SocketRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SocketController
@Slf4j
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatService chatService;
    private final SocketAuthenticationService authService;
    private final SocketMessageService messageService;
    private final SocketRoomService roomService;

    @SocketMapping(endpoint = "send_message", requestCls = ChatMessageDTO.class)
    public void handleSendMessage(SocketIOClient senderClient, SocketIOServer server, ChatMessageDTO data) {
        try {
            // 1. 인증 및 권한 검증 (역할 분리)
            Long senderId = authService.validateAndGetUserId(senderClient, data.roomId());

            // 2. 메시지 처리 (책임 분리)
            ChatMessageDTO savedMessage = messageService.processMessage(senderId, data);

            // 3. 실시간 전송 (협력)
            messageService.broadcastMessage(server, savedMessage, senderClient.getSessionId());

        } catch (BusinessException e) {
            senderClient.sendEvent("error", e.getMessage());
            log.warn("인증 실패: {}", e.getMessage());
        } catch (Exception e) {
            senderClient.sendEvent("error", "메시지 전송에 실패했습니다");
            log.error("메시지 전송 중 오류 발생", e);
        }
    }

    @SocketMapping(endpoint = "read_message", requestCls = ReadMessageRequest.class)
    public void handleReadMessage(SocketIOClient client, SocketIOServer server, ReadMessageRequest request) {
        try {
            Long memberId = authService.validateAndGetUserId(client, request.roomId());
            messageService.markAsRead(memberId, request);
        } catch (Exception e) {
            log.error("읽음 처리 중 오류 발생: memberId={}", client.get("memberId"), e);
        }
    }

    @SocketMapping(endpoint = "leave_room", requestCls = LeaveRoomRequest.class)
    public void handleLeaveRoom(SocketIOClient client, SocketIOServer server, LeaveRoomRequest request) {
        try {
            Long memberId = authService.validateUserId(client);
            roomService.leaveRoom(client, server, memberId, request.roomId());
        } catch (Exception e) {
            log.error("방 나가기 중 오류 발생: memberId={}", client.get("memberId"), e);
        }
    }

    @SocketMapping(endpoint = "join_room", requestCls = JoinRoomRequest.class)
    public void handleJoinRoom(SocketIOClient client, SocketIOServer server, JoinRoomRequest request) {
        try {
            Long memberId = authService.validateAndGetUserId(client, request.roomId());
            roomService.joinRoom(client, memberId, request.roomId());
        } catch (Exception e) {
            log.error("방 입장 중 오류 발생: memberId={}", client.get("memberId"), e);
        }
    }
}

