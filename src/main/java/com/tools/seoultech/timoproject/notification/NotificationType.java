package com.tools.seoultech.timoproject.notification;

public enum NotificationType {
	RANKING_REGISTERED("🎉 랭킹이 등록되었습니다. 순위를 확인해보세요!"),
	RANKING_UPDATED("랭킹이 업데이트되었습니다."),

	// 신청 관련
	SCRIM_APPLY("👥 %s님이 내전 신청하였습니다."),
	DUO_APPLY("🎮 %s님이 듀오 신청하였습니다."),

	// 수락 관련
	SCRIM_ACCEPTED("👥 %s님과 내전 신청이 수락되었습니다."),
	DUO_ACCEPTED("🎮 %s님과 듀오 신청이 수락되었습니다."),

	// 거절 관련
	SCRIM_REJECTED("👥 %s님과 내전 신청이 거절되었습니다."),
	DUO_REJECTED("🎮 %s님과 듀오 신청이 거절되었습니다."),

	CHAT_ROOM_CREATED("채팅방이 생성되었습니다.");

	private final String messageTemplate;

	NotificationType(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}

	public String getMessageTemplate() {
		return messageTemplate;
	}

	/**
	 * 닉네임을 포함한 메시지 생성
	 */
	public String formatMessage(String nickname) {
		if (nickname == null || nickname.trim().isEmpty()) {
			nickname = "알 수 없는 사용자";
		}
		return String.format(messageTemplate, nickname);
	}

	/**
	 * 기본 메시지 (닉네임이 필요 없는 경우)
	 */
	public String getDefaultMessage() {
		return messageTemplate;
	}
}
