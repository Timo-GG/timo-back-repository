package com.tools.seoultech.timoproject.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketAuthenticationService {

    private final ChatService chatService;

    public Long validateAndGetUserId(SocketIOClient client, Long roomId) {
        Long memberId = validateUserId(client);
        validateRoomAccess(memberId, roomId);
        return memberId;
    }

    public Long validateUserId(SocketIOClient client) {
        Long memberId = client.get("memberId");
        if (memberId == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }
        return memberId;
    }

    private void validateRoomAccess(Long memberId, Long roomId) {
        if (!chatService.isUserInRoom(memberId, roomId)) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}

