package com.tools.seoultech.timoproject.notification;

public record NotificationRequest(
	NotificationType type,
	String redirectUrl
) {}
