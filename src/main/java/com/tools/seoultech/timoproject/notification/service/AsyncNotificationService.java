package com.tools.seoultech.timoproject.notification.service;

import com.tools.seoultech.timoproject.notification.dto.NotificationRequest;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncNotificationService {
    private final NotificationService notificationService;

    @Async("notificationTaskExecutor")
    public void sendMatchingApplyNotificationAsync(Long acceptorId, String requestorNickname, NotificationType notificationType) {
        try {
            log.info("매칭 신청 알림 전송 시작 - acceptorId: {}, type: {}", acceptorId, notificationType);

            NotificationRequest request = new NotificationRequest(
                    notificationType,
                    "/mypage",
                    requestorNickname
            );

            notificationService.sendNotification(acceptorId, request);

            log.info("매칭 신청 알림 전송 완료 - acceptorId: {}, requestorNickname: {}",
                    acceptorId, requestorNickname);

        } catch (Exception e) {
            log.error("매칭 신청 알림 전송 실패 - acceptorId: {}, type: {}",
                    acceptorId, notificationType, e);
        }
    }

    @Async("notificationTaskExecutor")
    public void sendMatchingResultNotificationAsync(Long requestorId, String acceptorName,
                                                    NotificationType notificationType, String redirectUrl) {
        try {
            log.info("매칭 결과 알림 전송 시작 - requestorId: {}, type: {}", requestorId, notificationType);

            NotificationRequest request = new NotificationRequest(
                    notificationType,
                    redirectUrl,
                    acceptorName
            );

            notificationService.sendNotification(requestorId, request);

            log.info("매칭 결과 알림 전송 완료 - requestorId: {}, acceptorName: {}",
                    requestorId, acceptorName);

        } catch (Exception e) {
            log.error("매칭 결과 알림 전송 실패 - requestorId: {}, type: {}",
                    requestorId, notificationType, e);
        }
    }

    @Async("notificationTaskExecutor")
    public void sendRankingNotificationAsync(Long memberId, NotificationType notificationType, String redirectUrl) {
        try {
            log.info("랭킹 알림 전송 시작 - memberId: {}, type: {}", memberId, notificationType);

            NotificationRequest request = new NotificationRequest(notificationType, redirectUrl);
            notificationService.sendNotification(memberId, request);

            log.info("랭킹 알림 전송 완료 - memberId: {}", memberId);
        } catch (Exception e) {
            log.error("랭킹 알림 전송 실패 - memberId: {}, type: {}", memberId, notificationType, e);
        }
    }
}
