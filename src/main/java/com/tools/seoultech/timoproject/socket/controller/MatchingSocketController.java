package com.tools.seoultech.timoproject.socket.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.match.dto.MatchNotificationDTO;
import com.tools.seoultech.timoproject.match.dto.MatchResponseDTO;
import com.tools.seoultech.timoproject.match.dto.MatchResponseStatus;
import com.tools.seoultech.timoproject.match.dto.MatchResult;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;
import com.tools.seoultech.timoproject.match.service.MatchingService;
import com.tools.seoultech.timoproject.global.annotation.SocketController;
import com.tools.seoultech.timoproject.global.annotation.SocketMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@SocketController
@Slf4j
@RequiredArgsConstructor
public class MatchingSocketController {

    private final MatchingService matchingService;

    @SocketMapping(endpoint = "match_start", requestCls = MatchingOptionRequest.class)
    public void handleStartMatch(SocketIOClient client, SocketIOServer server, MatchingOptionRequest request) {
        Long memberId = client.get("memberId");
        log.info("[match_start] memberId={}, request={}", memberId, request);
        var matchIdOpt = matchingService.startMatch(memberId, request);
        if (matchIdOpt.isPresent()) {
            String matchId = matchIdOpt.get();
            Set<Long> memberIds = matchingService.getMatchMemberIds(matchId);
            if (memberIds != null && memberIds.size() == 2) {
                for (Long id : memberIds) {
                    Long opponentId = memberIds.stream().filter(m -> !m.equals(id)).findFirst().orElse(null);
                    notifyMatchFound(server, id, opponentId, matchId);
                }
            }
        } else {
            client.sendEvent("match_waiting", "매칭 대기중입니다.");
            log.info("Member {} is waiting for a match.", memberId);
        }
    }

    public void notifyMatchFound(SocketIOServer server, Long memberId, Long opponentId, String matchId) {
        MatchNotificationDTO notification = MatchNotificationDTO.builder()
                .matchId(matchId)
                .opponentId(opponentId)
                .message("매칭이 잡혔습니다. 매칭 요청에 응답해주세요.")
                .build();
        server.getRoomOperations("member_" + memberId.toString()).sendEvent("match_found", notification);
        log.info("Notified member {} of match {} with opponent {}.", memberId, matchId, opponentId);
    }

    @SocketMapping(endpoint = "match_response", requestCls = MatchResponseDTO.class)
    public void handleMatchResponse(SocketIOClient client, SocketIOServer server, MatchResponseDTO data) {
        Long memberId = client.get("memberId");
        String matchId = data.matchId();
        log.info("[match_response] memberId={}, matchId={}, accepted={}", memberId, matchId, data.isAccepted());

        if (data.isAccepted()) {
            MatchResult result = matchingService.acceptMatch(matchId, memberId);
            switch (result.status()) {
                case ACCEPTED:
                    Long chatRoomId = result.chatRoomId();
                    String roomName = "chat_" + chatRoomId.toString();

                    for (Long id : result.memberIds()) {
                        SocketIOClient targetClient = getClientByMemberId(server, id);
                        if (targetClient != null) {
                            targetClient.joinRoom(roomName);
                            targetClient.sendEvent("match_accepted", chatRoomId);
                            log.info("Member {} joined chat room {}.", id, chatRoomId);
                        } else {
                            log.warn("Member {} 클라이언트를 찾을 수 없습니다.", id);
                        }
                    }
                    break;
                case DECLINED:
                    client.sendEvent("match_declined", "상대방이 매칭을 취소하였습니다. 매칭이 취소되었습니다.");
                    log.info("Member {}: 매칭 {}가 상대방에 의해 취소됨.", memberId, matchId);
                    break;
                case PENDING:
                    client.sendEvent("match_pending", "상대방 응답 대기중입니다.");
                    log.info("Member {} accepted match {} but waiting for the other response.", memberId, matchId);
                    break;
            }
        } else {
            Set<Long> matchMemberIds = matchingService.getMatchMemberIds(matchId);
            matchingService.denyMatch(matchId, memberId);
            client.sendEvent("match_declined", "매칭이 취소되었습니다.");
            if (matchMemberIds != null) {
                for (Long otherMemberId : matchMemberIds) {
                    if (!otherMemberId.equals(memberId)) {
                        SocketIOClient targetClient = getClientByMemberId(server, otherMemberId);
                        if (targetClient != null) {
                            targetClient.sendEvent("match_declined", "상대방이 매칭을 거절하였습니다. 매칭이 취소되었습니다.");
                        }
                    }
                }
            }
            log.info("Member {} declined match {}.", memberId, matchId);
        }
    }

    /**
     * 서버에 연결된 모든 클라이언트 중, 주어진 memberId를 가진 클라이언트를 반환합니다.
     */
    private SocketIOClient getClientByMemberId(SocketIOServer server, Long memberId) {
        for (SocketIOClient client : server.getAllClients()) {
            Long id = client.get("memberId");
            if (memberId.equals(id)) {
                return client;
            }
        }
        return null;
    }
}
