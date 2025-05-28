package com.tools.seoultech.timoproject.chat.dto;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import lombok.Builder;

@Builder
public record ChatRoomResponse(
        Long roomId,
        String lastMessage,
        String opponentGameName,
        String opponentTagLine,
        String opponentProfileUrl,
        String opponentUnivName,
        Integer unreadCount,
        String lastMessageTime
) {

    public static ChatRoomResponse of(ChatRoom chatRoom, ChatRoomMember currentMember, ChatRoomMember opponent) {
        Member opponentEntity = opponent != null ? opponent.getMember() : null;
        RiotAccount riotAccount = opponentEntity != null ? opponentEntity.getRiotAccount() : null;
        CertifiedUnivInfo univInfo = opponentEntity != null ? opponentEntity.getCertifiedUnivInfo() : null;

        return ChatRoomResponse.builder()
                .roomId(chatRoom.getId())
                .lastMessage(chatRoom.getLastMessageContent())
                .opponentGameName(riotAccount != null ? riotAccount.getGameName() : "알 수 없음")
                .opponentTagLine(riotAccount != null ? riotAccount.getTagLine() : "")
                .opponentProfileUrl(riotAccount != null ? riotAccount.getProfileUrl() : null)
                .opponentUnivName(univInfo != null ? univInfo.getUnivName() : "미인증")
                .unreadCount(currentMember != null ? currentMember.getUnreadCount() : 0) // ✅ 실제 안 읽은 메시지 수
                .lastMessageTime(chatRoom.getLastMessageTime() != null ?
                        chatRoom.getLastMessageTime().toString() : null)
                .build();
    }
}


