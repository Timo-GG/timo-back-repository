package com.tools.seoultech.timoproject.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.chat.dto.ChatSocketDTO;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketRoomService {

    private final ChatService chatService;

    public void joinRoom(SocketIOClient client, Long memberId, Long roomId) {
        String roomName = createRoomName(roomId);

        client.joinRoom(roomName);
        chatService.joinRoom(memberId, roomId);

        log.info("[join_room] memberId={} joined room={}", memberId, roomName);
    }

    public void leaveRoom(SocketIOClient client, SocketIOServer server, Long memberId, Long roomId) {
        String roomName = createRoomName(roomId);

        log.info("[leave_room] memberId={}, roomName={}", memberId, roomName);

        client.leaveRoom(roomName);

        ChatSocketDTO<String> systemEventDTO = ChatSocketDTO.<String>builder()
                .eventType("leave")
                .roomId(roomId)
                .opponentId(memberId)
                .payload("Member " + memberId + " left the room: " + roomName)
                .build();

        server.getRoomOperations(roomName).sendEvent("leave", systemEventDTO);

        log.info("[leave_room] {}번 회원이 {} 방에서 나갔습니다.", memberId, roomName);
    }

    private String createRoomName(Long roomId) {
        return "chat_" + roomId;
    }
}

