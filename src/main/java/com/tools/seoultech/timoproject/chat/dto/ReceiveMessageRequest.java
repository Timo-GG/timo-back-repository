package com.tools.seoultech.timoproject.chat.dto;

import lombok.Builder;

@Builder
public record ReceiveMessageRequest(long senderId, String content, String room) {

}
