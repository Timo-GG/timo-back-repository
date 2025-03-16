package com.tools.seoultech.timoproject.chat.domain;

import com.tools.seoultech.timoproject.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lastMessageSenderId;

    private LocalDateTime lastMessageTime;

    private String lastMessageContent;

    private boolean isGroupChat = false;

    private boolean isTerminated;

    private String matchId;

    public static ChatRoom createRoom(String matchId) {
        ChatRoom room = new ChatRoom();
        room.matchId = matchId;
        return room;
    }

    public void updateLastMessage(Message message) {
        this.lastMessageSenderId = message.getSenderId();
        this.lastMessageTime = message.getRegDate();
        this.lastMessageContent = message.getContent();
    }

    public void terminate() {
        this.isTerminated = true;
    }


}
