package com.tools.seoultech.timoproject.notification.service;

import com.tools.seoultech.timoproject.notification.Notification;
import com.tools.seoultech.timoproject.notification.dto.NotificationChannelRequest;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailTemplateService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendHtmlEmail(Notification notification) {
        // 이메일 수신을 원하지 않는 사용자이거나, 이메일 주소가 없는 경우 전송하지 않음
        if (!notification.getMember().shouldReceiveEmailNotification() || notification.getMember().getEmailForNotification() == null) {
            log.warn("이메일 수신 거부 또는 주소 없음, 전송 건너뜀 - memberId: {}", notification.getMember().getMemberId());
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Notification 객체에서 직접 정보 추출
            helper.setTo(notification.getMember().getEmailForNotification());
            helper.setSubject(notification.getType().getCategory().getEmailSubject());
            helper.setFrom("mentenseoul@gmail.com");

            // HTML 템플릿 처리
            String htmlContent = processEmailTemplate(notification);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("HTML 이메일 전송 완료 - memberId: {}", notification.getMember().getMemberId());

        } catch (Exception e) {
            log.error("HTML 이메일 전송 실패 - memberId: {}", notification.getMember().getMemberId(), e);
        }
    }

    // 파라미터를 Notification 객체로 변경
    private String processEmailTemplate(Notification notification) {
        Context context = new Context();

        // Notification 객체에서 직접 정보 추출
        context.setVariable("username", notification.getMember().getUsername());
        context.setVariable("notification_title", getNotificationTitle(notification.getType()));
        context.setVariable("notification_message", notification.getMessage());
        context.setVariable("redirect_url", "https://timo.kr" + notification.getRedirectUrl());

        return templateEngine.process("EmailTemplate", context);
    }

    private String getNotificationTitle(NotificationType type) {
        return switch (type.getCategory()) {
            case MATCHING -> "새로운 매칭 알림";
            case CHAT -> "새로운 채팅 알림";
            case RANKING -> "랭킹 업데이트";
            case SYSTEM -> "시스템 알림";
        };
    }

    public JavaMailSender getMailSender() {
        return mailSender; // 테스트용으로 메일 발송기능을 직접 사용할 수 있도록 getter 제공
    }
}
