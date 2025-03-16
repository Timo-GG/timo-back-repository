package com.tools.seoultech.timoproject.chat.dto;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import lombok.Builder;

@Builder
public record ChatRoomResponse(Long roomId, String lastMessage) {
    public static ChatRoomResponse of(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .roomId(chatRoom.getId())
                .lastMessage(chatRoom.getLastMessageContent())
                .build();
    }
}
