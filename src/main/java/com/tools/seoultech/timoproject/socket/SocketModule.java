package com.tools.seoultech.timoproject.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.tools.seoultech.timoproject.auth.jwt.JwtResolver;
import com.tools.seoultech.timoproject.chat.service.ChatService;
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
            log.info("[disconnect] memberId = {}, sessionId = {}",
                    memberId, client.getSessionId());
        };
    }

    /**
     * 연결된 클라이언트에 대한 처리:
     * 1) 토큰 추출
     * 2) memberId 파싱
     * 3) 소켓 세션에 저장
     * 4) 방(room) 추출 & join
     * 5) connected_info 이벤트로 memberId 전송
     */
    private void handleConnection(SocketIOClient client) {
        String token = extractToken(client);
        Long memberId = parseMemberId(token);
        setMemberId(client, memberId);

        joinRoom(client, "member_" + memberId.toString());
        broadcastSystemMessage(memberId.toString(), memberId + " 님이 입장했습니다.");

        client.sendEvent("connected_info", memberId);

        log.info("[connect] Socket connected. memberId = {}, sessionId = {}, room = {}",
                memberId, client.getSessionId(), "member_" + memberId);
    }

    private String extractToken(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        log.info("[extractToken] token param = {}", token);
        if (token == null || token.isEmpty()) {
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
