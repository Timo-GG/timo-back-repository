package com.tools.seoultech.timoproject.notification.enumType;

public enum NotificationChannelType {
    SSE("Server-Sent Events", "실시간 웹 알림"),
    EMAIL("Email", "이메일 알림"),
    SMS("SMS", "문자 메시지 알림"),
    KAKAO("KakaoTalk", "카카오톡 알림"),
    DISCORD("Discord", "디스코드 알림"),
    SLACK("Slack", "슬랙 알림");

    private final String displayName;
    private final String description;

    NotificationChannelType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}