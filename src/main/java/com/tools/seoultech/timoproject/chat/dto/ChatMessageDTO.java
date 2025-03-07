package com.tools.seoultech.timoproject.chat.dto;

import lombok.Builder;

@Builder
public record ChatMessageDTO(Long messageId, Long roomId, Long senderId, String content) {
}
