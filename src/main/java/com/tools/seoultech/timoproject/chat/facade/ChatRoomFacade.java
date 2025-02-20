package com.tools.seoultech.timoproject.chat.facade;

import com.corundumstudio.socketio.SocketIOClient;
import com.tools.seoultech.timoproject.chat.dto.MessageResponse;
import com.tools.seoultech.timoproject.chat.dto.PageResult;

public interface ChatRoomFacade {
    MessageResponse sendTextMessage(
            SocketIOClient client,
            long memberId,
            long roomId,
            String content);

    void receiveMessage(
            SocketIOClient client,
            long memberId,
            long roomId,
            long messageId
    );

    PageResult<MessageResponse> getMessages(
            long memberId,
            long roomId,
            Long lastMessageId
    );

    Long getRecentMessageId(
            long memberId,
            long roomId
    );
}
