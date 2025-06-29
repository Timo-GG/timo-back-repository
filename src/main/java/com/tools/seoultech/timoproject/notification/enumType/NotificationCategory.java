package com.tools.seoultech.timoproject.notification.enumType;

public enum NotificationCategory {
    MATCHING("매칭", "게임 매칭 관련 알림", "[TIMO.GG] 매칭 알림"),
    CHAT("채팅", "채팅 관련 알림", "[TIMO.GG] 채팅 알림"),
    RANKING("랭킹", "랭킹 관련 알림", "[TIMO.GG] 랭킹 알림"),
    SYSTEM("시스템", "시스템 관련 알림", "[TIMO.GG] 시스템 알림");

    private final String displayName;
    private final String description;
    private final String emailSubject;

    NotificationCategory(String displayName, String description, String emailSubject) {
        this.displayName = displayName;
        this.description = description;
        this.emailSubject = emailSubject;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public String getEmailSubject() { return emailSubject; }
}

