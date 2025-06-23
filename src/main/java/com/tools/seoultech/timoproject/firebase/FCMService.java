package com.tools.seoultech.timoproject.firebase;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendChatNotification(Long chatRoomId, String senderName, String messageContent,
                                     List<String> targetTokens, Long senderId) {
        if (targetTokens.isEmpty()) {
            log.info("전송할 FCM 토큰이 없습니다.");
            return;
        }

        // 알림 메시지 구성
        Notification notification = Notification.builder()
                .setTitle(senderName + "님의 메시지")
                .setBody(messageContent.length() > 50 ?
                        messageContent.substring(0, 50) + "..." : messageContent)
                .build();

        // 여러 기기에 전송
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(notification)
                .putData("chatRoomId", String.valueOf(chatRoomId))
                .putData("senderId", String.valueOf(senderId))
                .putData("type", "chat_message")
                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                .addAllTokens(targetTokens)
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
                .setWebpushConfig(WebpushConfig.builder()
                        .putHeader("Urgency", "high")
                        .build())
                .build();

        try {
            BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
            log.info("FCM 알림 전송 성공: {}/{}", response.getSuccessCount(), targetTokens.size());

            // 실패한 토큰들 로깅
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        log.warn("FCM 전송 실패 - 토큰: {}, 오류: {}",
                                targetTokens.get(i), responses.get(i).getException().getMessage());
                    }
                }
            }
        } catch (FirebaseMessagingException e) {
            log.error("FCM 메시지 전송 중 오류 발생: {}", e.getMessage());
        }
    }
}
