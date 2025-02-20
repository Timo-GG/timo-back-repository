package com.tools.seoultech.timoproject.chat.dto;

import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.chat.model.MessageType;

public record MessageResponse(Long messageId, MessageType messageType,Long memberId, String content) {

    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getMessageType(),
                (message.getMember() != null) ? message.getMember().getId() : null,
                message.getContent()
        );
    }
}
