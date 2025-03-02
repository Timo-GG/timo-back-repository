package com.tools.seoultech.timoproject.chat.dto;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import lombok.Builder;

@Builder
public record ChatRoomResponse(Long chatRoomId, String roomName, String lastMessage) {
    public static ChatRoomResponse of(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .roomName(chatRoom.getChatRoomName())
                .lastMessage(chatRoom.getLastMessageContent())
                .build();
    }
}
