package com.tools.seoultech.timoproject.socket.service;

import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatNotificationService {

    private final ChatService chatService;
    private final MemberRepository memberRepository;

    @Async
    public void sendNotificationAsync(Long roomId, Long senderId, String content) {
        try {
            List<Long> recipientIds = getRecipientIds(roomId, senderId);

        } catch (Exception e) {
            log.error("FCM 알림 전송 실패: roomId={}, senderId={}", roomId, senderId, e);
        }
    }

    private List<Long> getRecipientIds(Long roomId, Long senderId) {
        return chatService.findActiveMembers(roomId).stream()
                .map(member -> member.getMember().getMemberId())
                .filter(id -> !id.equals(senderId))
                .collect(Collectors.toList());
    }

    private String getSenderName(Long senderId) {
        return memberRepository.findById(senderId)
                .map(member -> member.getUsername())
                .orElse("알 수 없는 사용자");
    }
}

