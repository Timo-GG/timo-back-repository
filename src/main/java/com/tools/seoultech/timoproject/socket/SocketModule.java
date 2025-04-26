package com.tools.seoultech.timoproject.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.tools.seoultech.timoproject.auth.jwt.JwtResolver;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketModule {

    private final SocketIOServer socketIOServer;
    private final JwtResolver jwtResolver;

    private int onlineCount = 0;

    @PostConstruct
    public void init() {
        // 1) 연결 리스너 (카운터 변경은 handleJoin에서)
        socketIOServer.addConnectListener(onConnected());
        // 2) 해제 리스너 (카운터 변경은 leave_online / onDisconnected 에서)
        socketIOServer.addDisconnectListener(onDisconnected());

        // 3) join_online 이벤트 직접 핸들링
        socketIOServer.addEventListener(
                "join_online",
                JoinOnlineRequest.class,
                (client, data, ack) -> {
                    onlineCount++;
                    log.info("[join_online] memberId={} 접속, 현재 접속자 수={}", data.getMemberId(), onlineCount);
                    socketIOServer.getBroadcastOperations()
                            .sendEvent("online_count", new OnlineCountResponse(onlineCount));
                    // 클라이언트 세션에 멤버 정보 보관
                    client.set("memberId", data.getMemberId());
                    client.set("isGuest", false);
                }
        );

        // 4) leave_online 이벤트 직접 핸들링
        socketIOServer.addEventListener(
                "leave_online",
                LeaveOnlineRequest.class,
                (client, data, ack) -> {
                    onlineCount = Math.max(0, onlineCount - 1);
                    log.info("[leave_online] memberId={} 퇴장, 현재 접속자 수={}", data.getMemberId(), onlineCount);
                    socketIOServer.getBroadcastOperations()
                            .sendEvent("online_count", new OnlineCountResponse(onlineCount));
                    client.set("memberId", null);
                }
        );

        // 5) 서버 시작
        socketIOServer.start();
        log.info("⚡ Socket server started.");
    }

    private ConnectListener onConnected() {
        return client -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            String guest = client.getHandshakeData().getSingleUrlParam("guest");
            if (token != null && !token.isEmpty()) {
                Long memberId = jwtResolver.getMemberIdFromAccessToken(token);
                client.set("memberId", memberId);
                client.set("isGuest", false);
                client.joinRoom("member_" + memberId);
                log.info("[connect] 로그인 유저 연결됨: memberId={}, sessionId={}", memberId, client.getSessionId());
            } else if ("true".equals(guest)) {
                client.set("isGuest", true);
                log.info("[connect] 게스트 유저 연결됨: sessionId={}", client.getSessionId());
            } else {
                log.warn("[connect] 토큰 또는 guest 파라미터 누락, 연결 거부");
                client.disconnect();
            }
            // **여기서는 카운터를 올리지 않습니다!**
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            Long memberId = client.get("memberId");
            Boolean isGuest = client.get("isGuest");
            log.info("[disconnect] memberId={}, sessionId={}", memberId, client.getSessionId());
            // 만약 leave_online 으로 빠져나가지 않은 경우, 여기서 한번 더 감소시켜 줌
            if (memberId != null || Boolean.TRUE.equals(isGuest)) {
                onlineCount = Math.max(0, onlineCount - 1);
                socketIOServer.getBroadcastOperations()
                        .sendEvent("online_count", new OnlineCountResponse(onlineCount));
            }
        };
    }

    // 이벤트 바인딩용 요청/응답 DTO
    public static class JoinOnlineRequest {
        private Long memberId;

        public Long getMemberId() {
            return memberId;
        }

        public void setMemberId(Long m) {
            this.memberId = m;
        }
    }

    public static class LeaveOnlineRequest {
        private Long memberId;

        public Long getMemberId() {
            return memberId;
        }

        public void setMemberId(Long m) {
            this.memberId = m;
        }
    }

    public static class OnlineCountResponse {
        private final int count;

        public OnlineCountResponse(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }
}
