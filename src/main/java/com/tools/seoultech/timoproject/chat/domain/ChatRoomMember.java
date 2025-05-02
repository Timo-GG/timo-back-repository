package com.tools.seoultech.timoproject.chat.domain;

import com.tools.seoultech.timoproject.memberAccount.domain.MemberAccount;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_room_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {

    @Id
    @Column(name = "chat_room_member_id")
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_account_id", nullable = false)
    private MemberAccount member;

    private int unreadCount;

    private Long lastReadMessageId;

    private boolean isLeft;

    public void increaseUnreadCount() {
        this.unreadCount++;
    }

    public void resetUnreadCount() {
        this.unreadCount = 0;
    }

    public void updateLastReadMessageId(Long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }

    public static ChatRoomMember createChatRoomMember(ChatRoom chatRoom, MemberAccount member) {
        ChatRoomMember chatRoomMember = new ChatRoomMember();
        chatRoomMember.chatRoom = chatRoom;
        chatRoomMember.member = member;
        chatRoomMember.unreadCount = 0;
        chatRoomMember.lastReadMessageId = 0L;

        return chatRoomMember;
    }

    public void leave() {
        this.isLeft = true;
    }

}
