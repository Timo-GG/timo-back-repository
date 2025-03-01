package com.tools.seoultech.timoproject.chat.domain;

import com.tools.seoultech.timoproject.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private Long senderId;

    private String content;

    public static Message createMessage(ChatRoom chatRoom, Long aLong, String content) {
        Message message = new Message();
        message.chatRoom = chatRoom;
        message.senderId = aLong;
        message.content = content;

        return message;
    }
}
