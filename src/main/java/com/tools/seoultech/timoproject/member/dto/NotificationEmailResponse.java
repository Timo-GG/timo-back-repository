package com.tools.seoultech.timoproject.member.dto;

import com.tools.seoultech.timoproject.member.domain.entity.Member;

public record NotificationEmailResponse(
        String notificationEmail,
        boolean emailNotificationsEnabled
) {
    public static NotificationEmailResponse from(Member member) {
        return new NotificationEmailResponse(
                member.getNotificationEmail(),
                member.shouldReceiveEmailNotification()
        );
    }
}