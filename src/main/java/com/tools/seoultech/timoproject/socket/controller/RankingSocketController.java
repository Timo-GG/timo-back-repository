package com.tools.seoultech.timoproject.socket.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.ranking.dto.Redis_RankingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RankingSocketController {

    private final SocketIOServer socketIOServer;

    public void broadcastRankingUpdate(List<Redis_RankingInfo> rankingList) {
        socketIOServer.getBroadcastOperations()
                .sendEvent("ranking_update", rankingList);
    }

    public void broadcastSingleUserUpdate(Redis_RankingInfo updatedUser) {
        socketIOServer.getBroadcastOperations().sendEvent(
                "ranking_user_update",
                updatedUser
        );
    }
}
