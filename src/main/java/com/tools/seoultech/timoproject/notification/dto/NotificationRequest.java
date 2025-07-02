package com.tools.seoultech.timoproject.notification.dto;

import com.tools.seoultech.timoproject.notification.enumType.NotificationType;

public record NotificationRequest(
		NotificationType type,
		String redirectUrl,
		String nickname
) {
	// 닉네임이 없는 경우를 위한 생성자
	public NotificationRequest(NotificationType type, String redirectUrl) {
		this(type, redirectUrl, null);
	}

	/**
	 * 포맷된 메시지 반환
	 */
	public String getFormattedMessage() {
		if (nickname != null && !nickname.trim().isEmpty()) {
			return type.formatMessage(nickname);
		}
		return type.getDefaultMessage();
	}
}