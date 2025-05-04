package com.tools.seoultech.timoproject.match.dto;

import java.util.Map;

public record MatchStatusResponse(String status, String matchId, String message, Map<Long, String> participantsStatus) {
}
