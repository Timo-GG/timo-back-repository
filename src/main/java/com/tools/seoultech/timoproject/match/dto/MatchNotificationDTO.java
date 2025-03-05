package com.tools.seoultech.timoproject.match.dto;

import lombok.Builder;

@Builder
public record MatchNotificationDTO(String matchId, Long opponentId, String message) {}
