package com.tools.seoultech.timoproject.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.tools.seoultech.timoproject.auth.jwt.JwtResolver;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketModule {

    private final SocketIOServer socketIOServer;
    private final JwtResolver jwtResolver;
    private final WebSocketAddMappingSupporter mappingSupporter;

    // 사용자별 연결 관리 (중복 방지)
    private final ConcurrentHashMap<Long, Set<String>> userSessions = new ConcurrentHashMap<>();
    // 게스트 세션 관리
    private final ConcurrentHashMap<String, UserSession> guestSessions = new ConcurrentHashMap<>();
    // 전체 온라인 카운터
    private final AtomicInteger onlineCount = new AtomicInteger(0);

    private final ExecutorService broadcastExecutor = Executors.newSingleThreadExecutor(
            r -> new Thread(r, "socket-broadcast-thread")
    );

    @PostConstruct
    public void init() {
        socketIOServer.addConnectListener(onConnected());
        socketIOServer.addDisconnectListener(onDisconnected());

        // join_online 이벤트는 선택적으로만 사용
        socketIOServer.addEventListener(
                "join_online",
                JoinOnlineRequest.class,
                this::handleJoinOnline
        );

        socketIOServer.addEventListener(
                "leave_online",
                LeaveOnlineRequest.class,
                this::handleLeaveOnline
        );

        mappingSupporter.addListeners(socketIOServer);
        socketIOServer.start();

        log.info("🔗 Socket server started with user-based counting");
    }

    private ConnectListener onConnected() {
        return client -> {
            try {
                String token = client.getHandshakeData().getSingleUrlParam("token");
                String guest = client.getHandshakeData().getSingleUrlParam("guest");

                if (token != null && !token.isEmpty()) {
                    handleAuthenticatedUser(client, token);
                } else if ("true".equals(guest)) {
                    handleGuestUser(client);
                } else {
                    // 토큰이 없어도 게스트로 처리 (기본 방문자)
                    handleGuestUser(client);
                }
            } catch (Exception e) {
                log.error("[connect] 연결 처리 중 오류 발생: sessionId={}", client.getSessionId(), e);
                // 오류가 발생해도 게스트로 처리
                handleGuestUser(client);
            }
        };
    }

    private void handleAuthenticatedUser(SocketIOClient client, String token) {
        try {
            Long memberId = jwtResolver.getMemberIdFromAccessToken(token);
            String sessionId = client.getSessionId().toString();

            // 기존 연결 확인 및 새 연결 추가
            boolean isFirstConnection = addUserSession(memberId, sessionId);

            client.set("memberId", memberId);
            client.set("isGuest", false);
            client.joinRoom("member_" + memberId);

            if (isFirstConnection) {
                // 첫 번째 연결일 때만 카운트 증가
                int currentCount = onlineCount.incrementAndGet();
                safeBroadcast("online_count", new OnlineCountResponse(currentCount));
                log.info("[connect] 인증 유저 첫 연결: memberId={}, 현재 온라인={}", memberId, currentCount);
            } else {
                log.info("[connect] 인증 유저 추가 연결: memberId={}, sessionId={}", memberId, sessionId);
            }
            client.sendEvent("online_count", new OnlineCountResponse(onlineCount.get()));

        } catch (Exception e) {
            log.error("[connect] 토큰 검증 실패, 게스트로 처리: sessionId={}", client.getSessionId(), e);
            handleGuestUser(client);
        }
    }

    private void handleGuestUser(SocketIOClient client) {
        String sessionId = client.getSessionId().toString();
        UserSession guestSession = new UserSession(null, true, System.currentTimeMillis());

        guestSessions.put(sessionId, guestSession);
        client.set("isGuest", true);

        // 게스트도 즉시 카운팅
        int currentCount = onlineCount.incrementAndGet();
        safeBroadcast("online_count", new OnlineCountResponse(currentCount));

        log.info("[connect] 게스트 유저 연결: sessionId={}, 현재 온라인={}", sessionId, currentCount);
    }

    // 사용자별 세션 관리 (중복 방지)
    private boolean addUserSession(Long memberId, String sessionId) {
        return userSessions.compute(memberId, (key, sessions) -> {
            if (sessions == null) {
                sessions = ConcurrentHashMap.newKeySet();
            }
            sessions.add(sessionId);
            return sessions;
        }).size() == 1; // 첫 번째 연결인지 반환
    }

    private boolean removeUserSession(Long memberId, String sessionId) {
        AtomicBoolean isLastConnection = new AtomicBoolean(false);

        userSessions.computeIfPresent(memberId, (key, sessions) -> {
            sessions.remove(sessionId);
            if (sessions.isEmpty()) {
                isLastConnection.set(true);
                return null; // Map에서 제거
            }
            return sessions;
        });

        return isLastConnection.get();
    }

    private void handleJoinOnline(SocketIOClient client, JoinOnlineRequest data, Object ack) {
        // 이미 연결 시 카운팅되므로 추가 처리 불필요
        log.info("[join_online] 이벤트 수신: memberId={} (이미 카운팅됨)", data.getMemberId());
        client.sendEvent("online_count", new OnlineCountResponse(onlineCount.get()));

    }

    private void handleLeaveOnline(SocketIOClient client, LeaveOnlineRequest data, Object ack) {
        // 연결 해제 시 자동 처리되므로 추가 처리 불필요
        log.info("[leave_online] 이벤트 수신: memberId={} (연결 해제 시 자동 처리)", data.getMemberId());
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            try {
                String sessionId = client.getSessionId().toString();
                Long memberId = client.get("memberId");
                Boolean isGuest = client.get("isGuest");

                if (memberId != null) {
                    // 인증된 사용자 처리
                    boolean isLastConnection = removeUserSession(memberId, sessionId);

                    if (isLastConnection) {
                        // 마지막 연결이 끊어졌을 때만 카운트 감소
                        int currentCount = onlineCount.decrementAndGet();
                        safeBroadcast("online_count", new OnlineCountResponse(currentCount));
                        log.info("[disconnect] 인증 유저 완전 연결 해제: memberId={}, 현재 온라인={}",
                                memberId, currentCount);
                    } else {
                        log.info("[disconnect] 인증 유저 부분 연결 해제: memberId={}, sessionId={}",
                                memberId, sessionId);
                    }

                } else if (Boolean.TRUE.equals(isGuest)) {
                    // 게스트 사용자 처리
                    guestSessions.remove(sessionId);
                    int currentCount = onlineCount.decrementAndGet();
                    safeBroadcast("online_count", new OnlineCountResponse(currentCount));
                    log.info("[disconnect] 게스트 유저 연결 해제: sessionId={}, 현재 온라인={}",
                            sessionId, currentCount);
                }

            } catch (Exception e) {
                log.error("[disconnect] 연결 해제 처리 중 오류: sessionId={}", client.getSessionId(), e);
            }
        };
    }

    private void safeBroadcast(String eventName, Object data) {
        broadcastExecutor.submit(() -> {
            try {
                socketIOServer.getBroadcastOperations().sendEvent(eventName, data);
            } catch (Exception e) {
                log.error("[broadcast] 브로드캐스트 실패: event={}", eventName, e);
            }
        });
    }

    // 디버깅용 메서드들
    public int getCurrentOnlineCount() {
        return onlineCount.get();
    }

    public int getUniqueUserCount() {
        return userSessions.size();
    }

    public int getGuestCount() {
        return guestSessions.size();
    }

    @PreDestroy
    public void cleanup() {
        broadcastExecutor.shutdown();
        try {
            if (!broadcastExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                broadcastExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            broadcastExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("Socket module cleanup completed");
    }

    // 기존 DTO 클래스들은 동일하게 유지
    private static class UserSession {
        private final Long memberId;
        private final boolean isGuest;
        private final long connectedAt;

        public UserSession(Long memberId, boolean isGuest, long connectedAt) {
            this.memberId = memberId;
            this.isGuest = isGuest;
            this.connectedAt = connectedAt;
        }

        // getters...
    }

    public static class JoinOnlineRequest {
        private Long memberId;
        public Long getMemberId() { return memberId; }
        public void setMemberId(Long memberId) { this.memberId = memberId; }
    }

    public static class LeaveOnlineRequest {
        private Long memberId;
        public Long getMemberId() { return memberId; }
        public void setMemberId(Long memberId) { this.memberId = memberId; }
    }

    public static class OnlineCountResponse {
        private final int count;
        public OnlineCountResponse(int count) { this.count = count; }
        public int getCount() { return count; }
    }
}
