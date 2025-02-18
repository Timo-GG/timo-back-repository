package com.tools.seoultech.timoproject.chat.model.dto;

import com.tools.seoultech.timoproject.chat.model.MessageType;

public record MessageResponse(Long messageId, MessageType messageType,Long memberId, String content) {

}
