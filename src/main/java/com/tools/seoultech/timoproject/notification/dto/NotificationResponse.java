package com.tools.seoultech.timoproject.notification.dto;

import com.tools.seoultech.timoproject.notification.Notification;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
	Long id,
	NotificationType type,
	String message,
	String redirectUrl,
	boolean isRead,
	LocalDateTime regDate
) {
	public static NotificationResponse from(Notification notification) {
		return new NotificationResponse(
			notification.getId(),
			notification.getType(),
			notification.getMessage(),
			notification.getRedirectUrl(),
			notification.isRead(),
				notification.getRegDate()
		);
	}
}
