package com.tools.seoultech.timoproject.chat.dto;

import lombok.Builder;

@Builder
public record ChatMessageDTO(Long messageId, String room, Long senderId, String content) {
}
