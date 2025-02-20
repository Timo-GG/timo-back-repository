package com.tools.seoultech.timoproject.chat.dto;

public record GetLastSeenMessageResponse(Long messageId) {
    public static GetLastSeenMessageResponse from(Long messageId) {
        return new GetLastSeenMessageResponse(messageId);
    }
}
