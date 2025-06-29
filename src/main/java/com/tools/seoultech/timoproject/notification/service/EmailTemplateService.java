package com.tools.seoultech.timoproject.notification.service;

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

    public void sendHtmlEmail(NotificationChannelRequest request, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.memberEmail());
            helper.setSubject(request.type().getCategory().getEmailSubject());
            helper.setFrom("mentenseoul@gmail.com");

            // HTML 템플릿 처리
            String htmlContent = processEmailTemplate(request, username);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("HTML 이메일 전송 완료 - memberId: {}", request.memberId());

        } catch (Exception e) {
            log.error("HTML 이메일 전송 실패 - memberId: {}", request.memberId(), e);
        }
    }

    private String processEmailTemplate(NotificationChannelRequest request, String username) {
        // 템플릿 변수 설정
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("notification_title", getNotificationTitle(request.type()));
        context.setVariable("notification_message", request.message());
        context.setVariable("redirect_url", "https://timo.kr" + request.redirectUrl());

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
