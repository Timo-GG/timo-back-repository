package com.tools.seoultech.timoproject.notification.dto;

import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;

public record NotificationChannelRequest(
        Long memberId,
        String memberEmail,
        NotificationType type,
        String message,
        String redirectUrl
) {
    public static NotificationChannelRequest from(Member member, NotificationRequest request, String formattedMessage) {
        return new NotificationChannelRequest(
                member.getMemberId(),
                member.getEmailForNotification(),
                request.type(),
                formattedMessage,
                request.redirectUrl()
        );
    }
}
