package com.tools.seoultech.timoproject.notification.enumType;

// 2. NotificationType을 단순화하고 카테고리와 연결
public enum NotificationType {
	// 랭킹 관련
	RANKING_REGISTERED("🎉 랭킹이 등록되었습니다. 순위를 확인해보세요!", NotificationCategory.RANKING, true),
	RANKING_UPDATED("📊 랭킹이 업데이트되었습니다.", NotificationCategory.RANKING, false),

	// 매칭 신청 관련
	SCRIM_APPLY("👥 %s님이 내전 신청하였습니다.", NotificationCategory.MATCHING, true),
	DUO_APPLY("🎮 %s님이 듀오 신청하였습니다.", NotificationCategory.MATCHING, true),

	// 매칭 수락 관련
	SCRIM_ACCEPTED("✅ %s님과 내전 신청이 수락되었습니다.", NotificationCategory.MATCHING, true),
	DUO_ACCEPTED("✅ %s님과 듀오 신청이 수락되었습니다.", NotificationCategory.MATCHING, true),

	// 매칭 거절 관련
	SCRIM_REJECTED("❌ %s님과 내전 신청이 거절되었습니다.", NotificationCategory.MATCHING, true),
	DUO_REJECTED("❌ %s님과 듀오 신청이 거절되었습니다.", NotificationCategory.MATCHING, true),

	// 채팅 관련
	CHAT_ROOM_CREATED("💬 채팅방이 생성되었습니다.", NotificationCategory.CHAT, false);

	private final String messageTemplate;
	private final NotificationCategory category;
	private final boolean requiresEmailNotification;

	NotificationType(String messageTemplate, NotificationCategory category, boolean requiresEmailNotification) {
		this.messageTemplate = messageTemplate;
		this.category = category;
		this.requiresEmailNotification = requiresEmailNotification;
	}

	public String getMessageTemplate() { return messageTemplate; }
	public NotificationCategory getCategory() { return category; }
	public boolean requiresEmailNotification() { return requiresEmailNotification; }

	public String formatMessage(String nickname) {
		if (nickname == null || nickname.trim().isEmpty()) {
			nickname = "알 수 없는 사용자";
		}
		return String.format(messageTemplate, nickname);
	}

	public String getDefaultMessage() {
		return messageTemplate;
	}
}

