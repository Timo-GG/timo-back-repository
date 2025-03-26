package com.tools.seoultech.timoproject.chat.dto;

import lombok.Builder;

@Builder
public record ChatSocketDTO<T>(String eventType, Long roomId, Long opponentId, T payload) {
}
