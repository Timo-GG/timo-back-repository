package com.tools.seoultech.timoproject.match.dto;

import java.util.Set;

public record MatchResult(MatchResponseStatus status, Long chatRoomId, Set<Long> memberIds) {
}
