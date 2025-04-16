package com.tools.seoultech.timoproject.socket.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.global.annotation.SocketController;
import com.tools.seoultech.timoproject.global.annotation.SocketMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SocketController
@Slf4j
@RequiredArgsConstructor
public class OnlineStatusSocketController {

    private int onlineCount = 0;

    @SocketMapping(endpoint = "join_online", requestCls = JoinOnlineRequest.class)
    public void handleJoin(SocketIOClient client, SocketIOServer server, JoinOnlineRequest request) {
        Long memberId = request.memberId(); // 로그인 유저만 사용
        client.set("memberId", memberId);
        client.set("isGuest", false); // 명시적으로 설정 (선택)

        onlineCount++;
        log.info("[join_online] memberId={} 접속, 현재 접속자 수={}", memberId, onlineCount);
        server.getBroadcastOperations().sendEvent("online_count", new OnlineCountResponse(onlineCount));
    }

    @SocketMapping(endpoint = "leave_online", requestCls = LeaveOnlineRequest.class)
    public void handleLeave(SocketIOClient client, SocketIOServer server, LeaveOnlineRequest request) {
        Long memberId = request.memberId();
        client.set("memberId", null);

        onlineCount = Math.max(0, onlineCount - 1);
        log.info("[leave_online] memberId={} 퇴장, 현재 접속자 수={}", memberId, onlineCount);
        server.getBroadcastOperations().sendEvent("online_count", new OnlineCountResponse(onlineCount));
    }

    public void onDisconnect(SocketIOClient client) {
        onlineCount = Math.max(0, onlineCount - 1);
        log.info("[disconnect] sessionId = {}, 현재 접속자 수={}", client.getSessionId(), onlineCount);
        client.getNamespace().getBroadcastOperations().sendEvent("online_count",
                new OnlineCountResponse(onlineCount));
    }

    public record JoinOnlineRequest(Long memberId) {}
    public record LeaveOnlineRequest(Long memberId) {}
    public record OnlineCountResponse(int count) {}
}