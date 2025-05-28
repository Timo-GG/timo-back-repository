package com.tools.seoultech.timoproject.notification;

public record NotificationResponse(
	Long id,
	NotificationType type,
	String message,
	String redirectUrl,
	boolean isRead
) {
	public static NotificationResponse from(Notification notification) {
		return new NotificationResponse(
			notification.getId(),
			notification.getType(),
			notification.getMessage(),
			notification.getRedirectUrl(),
			notification.isRead()
		);
	}
}
