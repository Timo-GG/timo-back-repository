package com.tools.seoultech.timoproject.notification;

public enum NotificationType {
	RANKING_REGISTERED("랭킹 등록이 완료되었습니다."),
	RANKING_UPDATED("랭킹이 업데이트되었습니다."),
	SCRIM_APPLY("내전 신청이 접수되었습니다."),
	SCRIM_ACCEPTED("내전 신청이 수락되었습니다."),
	SCRIM_REJECTED("내전 신청이 거절되었습니다."),
	DUO_APPLY("듀오 신청이 접수되었습니다."),
	DUO_ACCEPTED("듀오 신청이 수락되었습니다."),
	DUO_REJECTED("듀오 신청이 거절되었습니다."),
	CHAT_ROOM_CREATED("채팅방이 생성되었습니다.");

	private final String defaultMessage;

	NotificationType(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}
