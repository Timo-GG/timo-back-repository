package com.tools.seoultech.timoproject.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record NotificationEmailRequest(
        @Email(message = "올바른 이메일 형식이 아닙니다")
        @Size(max = 255, message = "이메일은 255자를 초과할 수 없습니다")
        String notificationEmail
) {
    /**
     * 이메일 알림 비활성화를 위한 생성자
     */
    public static NotificationEmailRequest disable() {
        return new NotificationEmailRequest(null);
    }

    /**
     * 이메일 알림 활성화를 위한 생성자
     */
    public static NotificationEmailRequest enable(String email) {
        return new NotificationEmailRequest(email);
    }

    /**
     * 이메일 알림이 활성화되는지 확인
     */
    public boolean isEmailNotificationEnabled() {
        return notificationEmail != null && !notificationEmail.trim().isEmpty();
    }
}