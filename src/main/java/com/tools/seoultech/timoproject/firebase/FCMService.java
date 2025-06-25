package com.tools.seoultech.timoproject.firebase;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;
    private final FCMTokenService fcmTokenService;

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

            if (response.getFailureCount() > 0) {
                List<String> invalidTokens = new ArrayList<>();
                List<SendResponse> responses = response.getResponses();

                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        String errorMessage = responses.get(i).getException().getMessage();
                        String token = targetTokens.get(i);

                        if (errorMessage.contains("Requested entity was not found") ||
                                errorMessage.contains("UNREGISTERED") ||
                                errorMessage.contains("registration-token-not-registered")) {
                            invalidTokens.add(token);
                            log.warn("만료된 FCM 토큰 발견: {}", token);
                        } else {
                            log.warn("FCM 전송 실패 - 토큰: {}, 오류: {}", token, errorMessage);
                        }
                    }
                }

                if (!invalidTokens.isEmpty()) {
                    try {
                        fcmTokenService.deactivateTokens(invalidTokens);
                        log.info("{}개의 만료된 FCM 토큰을 비활성화했습니다", invalidTokens.size());
                    } catch (Exception e) {
                        log.error("FCM 토큰 비활성화 중 오류 발생", e);
                    }
                }
            }
        } catch (FirebaseMessagingException e) {
            log.error("FCM 메시지 전송 중 오류 발생: {}", e.getMessage());
        }
    }
}
