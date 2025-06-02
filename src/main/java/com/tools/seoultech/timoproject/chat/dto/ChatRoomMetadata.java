package com.tools.seoultech.timoproject.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 채팅방 메타데이터 캐싱용 DTO
 * - roomId: 어떤 채팅방인지
 * - lastMessage: 가장 최근 메시지 내용
 * - lastMessageTime: 가장 최근 메시지 시간
 * - lastMessageSenderId: 가장 최근 메시지 전송자
 * - unreadMap: <memberId, 증가해야 할 unreadCount 누적치>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomMetadata {
    private Long roomId;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Long lastMessageSenderId;

    // 예: memberId별로 얼마나 unreadCount가 증가했는지 누적
    private Map<Long, Integer> unreadMap = new HashMap<>();

    public void incrementUnread(Long memberId) {
        this.unreadMap.put(memberId, unreadMap.getOrDefault(memberId, 0) + 1);
    }
}
