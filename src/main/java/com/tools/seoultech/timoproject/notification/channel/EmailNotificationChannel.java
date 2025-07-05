package com.tools.seoultech.timoproject.notification.channel;

import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.notification.Notification;
import com.tools.seoultech.timoproject.notification.dto.NotificationChannelRequest;
import com.tools.seoultech.timoproject.notification.enumType.NotificationChannelType;
import com.tools.seoultech.timoproject.notification.service.EmailTemplateService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationChannel implements NotificationChannel {
    private final EmailTemplateService emailTemplateService;

    @Override
    public NotificationChannelType getChannelType() {
        return NotificationChannelType.EMAIL;
    }

    @Override
    public void send(Notification notification) {

        sendEmailAsync(notification);
    }

    @Async("notificationTaskExecutor")
    public void sendEmailAsync(Notification notification) {
        Member member = notification.getMember();
        if (!isEnabled(member, notification.getType())) {
            return;
        }

        try {
            log.info("이메일 전송 시작 - memberId: {}, type: {}", member.getMemberId(), notification.getType());

            // ⭐️ Notification 객체에서 직접 사용자 이름과 이메일 추출
            String username = member.getUsername();
            String emailAddress = member.getEmailForNotification();

            // emailTemplateService의 메소드도 Notification 객체를 받도록 수정하거나,
            // 필요한 정보만 넘기도록 수정합니다.
            emailTemplateService.sendHtmlEmail(notification);

            log.info("이메일 알림 전송 완료 - memberId: {}, type: {}", member.getMemberId(), notification.getType());

        } catch (Exception e) {
            log.error("이메일 전송 실패 - memberId: {}, type: {}", member.getMemberId(), notification.getType(), e);
        }
    }

    @Override
    public boolean isEnabled(Member member, NotificationType type) {
        return member.shouldReceiveEmailNotification() && type.requiresEmailNotification();
    }
}
