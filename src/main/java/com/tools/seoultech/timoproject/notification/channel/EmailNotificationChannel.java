package com.tools.seoultech.timoproject.notification.channel;

import com.tools.seoultech.timoproject.member.service.MemberService;
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
    private final MemberService memberService;

    @Override
    public NotificationChannelType getChannelType() {
        return NotificationChannelType.EMAIL;
    }

    @Override
    public void send(NotificationChannelRequest request) {
        // 🔥 비동기로 이메일 전송
        sendEmailAsync(request);
    }

    @Async("notificationTaskExecutor")
    public void sendEmailAsync(NotificationChannelRequest request) {
        if (request.memberEmail() == null) {
            log.warn("이메일 주소가 없어 전송 건너뜀 - memberId: {}", request.memberId());
            return;
        }

        try {
            log.info("이메일 전송 시작 - memberId: {}, type: {}",
                    request.memberId(), request.type());

            Member member = memberService.getById(request.memberId());
            String username = member.getUsername();

            emailTemplateService.sendHtmlEmail(request, username);

            log.info("이메일 알림 전송 완료 - memberId: {}, type: {}",
                    request.memberId(), request.type());

        } catch (Exception e) {
            log.error("이메일 전송 실패 - memberId: {}, type: {}",
                    request.memberId(), request.type(), e);
        }
    }

    @Override
    public boolean isEnabled(Member member, NotificationType type) {
        return member.shouldReceiveEmailNotification() && type.requiresEmailNotification();
    }
}
