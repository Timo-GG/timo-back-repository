package com.tools.seoultech.timoproject.chat.domain;

import com.tools.seoultech.timoproject.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String chatRoomName;

    private Long lastMessageSenderId;

    private LocalDateTime lastMessageTime;

    private String lastMessageContent;

    private boolean isGroupChat = false;

    public static ChatRoom createRoom(String chatRoomName) {
        ChatRoom room = new ChatRoom();
        room.chatRoomName = chatRoomName;
        // 초기값, 검증 등...
        return room;
    }

}
