package com.tools.seoultech.timoproject.notification.channel;

import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.notification.dto.NotificationChannelRequest;
import com.tools.seoultech.timoproject.notification.enumType.NotificationChannelType;
import com.tools.seoultech.timoproject.notification.service.EmailTemplateService;
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
        if (request.memberEmail() == null) {
            log.warn("이메일 주소가 없어 전송 건너뜀 - memberId: {}", request.memberId());
            return;
        }

        try {
            // 사용자 정보 조회 (username 필요)
            Member member = memberService.getById(request.memberId());
            String username = member.getUsername();

            // HTML 템플릿으로 이메일 전송
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
