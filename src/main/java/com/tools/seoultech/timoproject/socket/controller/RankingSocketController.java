package com.tools.seoultech.timoproject.socket.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.ranking.dto.RedisRankingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RankingSocketController {

    private final SocketIOServer socketIOServer;

    public void broadcastRankingUpdate(List<RedisRankingInfo> rankingList) {
        socketIOServer.getBroadcastOperations()
                .sendEvent("ranking_update", rankingList);
    }

    public void broadcastSingleUserUpdate(RedisRankingInfo updatedUser) {
        socketIOServer.getBroadcastOperations().sendEvent(
                "ranking_user_update",
                updatedUser
        );
    }
}
