package com.tools.seoultech.timoproject.socket.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.match.dto.MatchNotificationDTO;
import com.tools.seoultech.timoproject.match.dto.MatchResponseDTO;
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
    private final ChatService chatService;

    /**
     * 클라이언트가 매칭 요청을 보내면 해당 회원의 매칭 요청을 처리하고,
     * 즉시 매칭이 이루어졌다면 두 회원에게 match_found 이벤트를 전달합니다.
     */
    @SocketMapping(endpoint = "match_start", requestCls = MatchingOptionRequest.class)
    public void handleStartMatch(SocketIOClient client, SocketIOServer server, MatchingOptionRequest request) {
        Long memberId = client.get("memberId");
        // 매칭 요청 처리 – 매칭이 바로 이루어졌다면 Optional에 matchId가 포함됨
        var matchIdOpt = matchingService.startMatch(memberId, request);
        if (matchIdOpt.isPresent()) {
            String matchId = matchIdOpt.get();
            // 매칭이 이루어진 경우, Redis에 저장된 매칭 정보를 이용해 두 회원의 ID를 조회합니다.
            // (매칭 서비스에서 matchKey에 두 회원의 ID가 저장되어 있다고 가정)
            Set<Long> memberIds = matchingService.getMatchMemberIds(matchId);
            if (memberIds != null && memberIds.size() == 2) {
                // 각 회원에게 자신의 고유 룸(memberId를 문자열로 사용)에 match_found 이벤트를 발송
                for (Long id : memberIds) {
                    // 상대 회원 ID는 나머지 하나입니다.
                    Long opponentId = memberIds.stream().filter(m -> !m.equals(id)).findFirst().orElse(null);
                    notifyMatchFound(server, id, opponentId, matchId);
                }
            }
        } else {
            // 매칭이 즉시 이루어지지 않은 경우, 클라이언트에 대기 메시지 전송
            client.sendEvent("match_waiting", "매칭 대기중입니다.");
            log.info("Member {} is waiting for a match.", memberId);
        }
    }

    /**
     * 매칭 결과를 알리는 메서드.
     * 각 클라이언트는 자신의 memberId를 문자열로 된 룸에 가입되어 있다고 가정합니다.
     */
    public void notifyMatchFound(SocketIOServer server, Long memberId, Long opponentId, String matchId) {
        MatchNotificationDTO notification = MatchNotificationDTO.builder()
                .matchId(matchId)
                .opponentId(opponentId)
                .message("매칭이 잡혔습니다. 매칭 요청에 응답해주세요.")
                .build();
        server.getRoomOperations(memberId.toString()).sendEvent("match_found", notification);
        log.info("Notified member {} of match {} with opponent {}.", memberId, matchId, opponentId);
    }

    /**
     * 클라이언트로부터 매칭 수락/거절 응답을 받는 엔드포인트.
     * 요청 DTO(MatchResponseDTO)에는 matchId와 accepted(boolean) 값이 포함되어 있습니다.
     */
    @SocketMapping(endpoint = "match_response", requestCls = MatchResponseDTO.class)
    public void handleMatchResponse(SocketIOClient client, SocketIOServer server, MatchResponseDTO data) {
        Long memberId = client.get("memberId"); // 소켓 세션에 저장된 회원 ID
        String matchId = data.matchId();
        log.info("[match_response] memberId={}, matchId={}, accepted={}", memberId, matchId, data.isAccepted());

        if (data.isAccepted()) {
            // 매칭 수락: MatchingService의 acceptMatch() 메서드를 호출합니다.
            boolean accepted = matchingService.acceptMatch(matchId, memberId);
            if (accepted) {
                // 두 명 모두 수락되어 채팅방이 생성된 경우.
                // 채팅방 이름은 매칭 서비스에서 생성된 채팅룸 정보를 사용합니다.
                ChatRoom chatRoom = chatService.findChatRoomByMatchId(matchId);
                if (chatRoom != null) {
                    String chatRoomName = chatRoom.getChatRoomName();
                    client.joinRoom(chatRoomName);
                    client.sendEvent("match_accepted", chatRoomName);
                    log.info("Member {} accepted match {}. Joined chat room {}", memberId, matchId, chatRoomName);
                } else {
                    client.sendEvent("match_error", "채팅방 생성에 실패했습니다.");
                    log.error("채팅방 생성 실패 matchId={}", matchId);
                }
            } else {
                // 아직 다른 사용자의 응답을 기다리는 경우.
                client.sendEvent("match_pending", "상대방 응답 대기중입니다.");
                log.info("Member {} accepted match {} but waiting for the other response.", memberId, matchId);
            }
        } else {
                // 매칭 거절 처리: 거절한 회원과 함께 매칭된 다른 회원에게도 'match_declined' 이벤트를 전송
                Set<Long> matchMemberIds = matchingService.getMatchMemberIds(matchId);
                matchingService.denyMatch(matchId, memberId);
                // 거절한 회원에게도 매칭 취소 알림 전송
                client.sendEvent("match_declined", "매칭이 취소되었습니다.");
                // 다른 회원에게도 전송 (예: 수락한 쪽도 취소 알림)
                if (matchMemberIds != null) {
                    for (Long otherMemberId : matchMemberIds) {
                        if (!otherMemberId.equals(memberId)) {
                            server.getRoomOperations(otherMemberId.toString())
                                    .sendEvent("match_declined", "상대방이 매칭을 거절하였습니다. 매칭이 취소되었습니다.");
                        }
                    }
                }
                log.info("Member {} declined match {}.", memberId, matchId);
        }
    }
}
