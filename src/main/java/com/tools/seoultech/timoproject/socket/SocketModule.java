package com.tools.seoultech.timoproject.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.tools.seoultech.timoproject.auth.jwt.JwtResolver;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.socket.controller.OnlineStatusSocketController;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketModule implements DisposableBean {

    private final SocketIOServer socketIOServer;
    private final JwtResolver jwtResolver;
    private final WebSocketAddMappingSupporter webSocketAddMappingSupporter;
    private static int onlineCount = 0;

    @PostConstruct
    public void initSocketServer() {
        webSocketAddMappingSupporter.addListeners(socketIOServer);
        socketIOServer.start();
        socketIOServer.addConnectListener(onConnected());
        socketIOServer.addDisconnectListener(onDisconnected());

    }

    @Override
    public void destroy() throws Exception {
        log.info("Shutting down SocketIOServer...");
        socketIOServer.stop();
    }

    private ConnectListener onConnected() {
        return client -> {
            try {
                handleConnection(client);
            } catch (Exception e) {
                log.error("[connect] Invalid token or connection error: {}", e.getMessage(), e);
                client.disconnect();
            }
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            Long memberId = client.get("memberId");
            Boolean isGuest = client.get("isGuest");

            log.info("[disconnect] memberId = {}, sessionId = {}", memberId, client.getSessionId());

            onlineCount = Math.max(0, onlineCount - 1);
            socketIOServer.getBroadcastOperations().sendEvent("online_count", new OnlineStatusSocketController.OnlineCountResponse(onlineCount));
        };
    }

    private void handleConnection(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        String guest = client.getHandshakeData().getSingleUrlParam("guest");

        Long memberId = null;

        if (token != null && !token.isEmpty()) {
            memberId = jwtResolver.getMemberIdFromAccessToken(token);
            client.set("memberId", memberId);
            client.set("isGuest", false);
            joinRoom(client, "member_" + memberId);
            broadcastSystemMessage("member_" + memberId, memberId + " 님이 입장했습니다.");
            client.sendEvent("connected_info", memberId);
            log.info("[connect] 로그인 유저 연결됨: memberId = {}, sessionId = {}", memberId, client.getSessionId());
            log.info("counter = {}", onlineCount);
        } else if ("true".equals(guest)) {
            client.set("isGuest", true);
            log.info("[connect] 게스트 유저 연결됨: sessionId = {}", client.getSessionId());
        } else {
            throw new IllegalArgumentException("Token 또는 guest=true 쿼리 파라미터가 필요합니다.");
        }

        // 모든 유저 대상 접속자 수 증가
        onlineCount++;
        socketIOServer.getBroadcastOperations().sendEvent("online_count", new OnlineStatusSocketController.OnlineCountResponse(onlineCount));
    }


    private String extractToken(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        String guest = client.getHandshakeData().getSingleUrlParam("guest");

        if (token == null || token.isEmpty()) {
            if ("true".equals(guest)) {
                log.info("[extractToken] 게스트 유저 접속 허용");
                return null; // 게스트는 토큰 없이 통과
            }
            throw new IllegalArgumentException("Token is missing or empty");
        }

        return token;
    }

    private Long parseMemberId(String token) {
        return jwtResolver.getMemberIdFromAccessToken(token);
    }

    private void setMemberId(SocketIOClient client, Long memberId) {
        client.set("memberId", memberId);
    }

    private void joinRoom(SocketIOClient client, String room) {
        client.joinRoom(room);
    }

    // 시스템 메시지 브로드캐스트
    private void broadcastSystemMessage(String room, String message) {
        socketIOServer.getRoomOperations(room).sendEvent("system_message", message);
    }

}
