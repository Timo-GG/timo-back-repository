package com.tools.seoultech.timoproject.chat.socket;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.tools.seoultech.timoproject.chat.LeaveRoomRequest;
import com.tools.seoultech.timoproject.chat.constant.Constants;
import com.tools.seoultech.timoproject.chat.model.Message;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class SocketModule {

    private final SocketIOServer server;
    private final SocketService socketService;

    @PostConstruct
    public void init() {
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", Message.class, onChatReceived());
        server.addEventListener("leave_room", LeaveRoomRequest.class, onLeaveRoomReceived());
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.saveMessage(senderClient, data);
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            var params = client.getHandshakeData().getUrlParams();
            String room = params.get("room").stream().collect(Collectors.joining());
            String username = params.get("username").stream().collect(Collectors.joining());
            client.joinRoom(room);
            socketService.saveInfoMessage(client, String.format(Constants.WELCOME_MESSAGE, username), room);
            log.info("Socket ID[{}] - room[{}] - username [{}] connected", client.getSessionId().toString(), room, username);
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            String room = params.get("room").stream().collect(Collectors.joining());
            String username = params.get("username").stream().collect(Collectors.joining());
            socketService.saveInfoMessage(client, String.format(Constants.DISCONNECT_MESSAGE, username), room);
            log.info("Socket ID[{}] - room[{}] - username [{}]  discnnected to chat module through",
                    client.getSessionId().toString(), room, username);

            // 만약 방에 아무도 남아 있지 않다면
            if (server.getRoomOperations(room).getClients().isEmpty()) {
                log.info("Room [{}] is now empty. Everyone left.", room);
            }
        };
    }

    private DataListener<LeaveRoomRequest> onLeaveRoomReceived() {
        return (client, data, ackSender) -> {
            String room = data.room();

            // 명시적으로 방을 떠나기
            client.leaveRoom(room);
            // 후처리(로그, DB 갱신, 다른 사용자에게 알림 등)
            socketService.saveInfoMessage(client, String.format(Constants.DISCONNECT_MESSAGE, data.username()), room);
            log.info("Socket ID[{}] - room[{}] - username [{}] left by 'leave_room' event",
                    client.getSessionId().toString(), room, data.username());

            // 만약 방에 아무도 남아 있지 않다면
            if (server.getRoomOperations(room).getClients().isEmpty()) {
                log.info("Room [{}] is now empty. Everyone left.", room);
            }
        };
    }

}
