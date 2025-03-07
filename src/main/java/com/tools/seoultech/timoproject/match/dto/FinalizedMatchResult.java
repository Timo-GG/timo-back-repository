package com.tools.seoultech.timoproject.match.dto;

import java.util.Set;

public record FinalizedMatchResult(Long chatRoomId, Set<Long> memberIds) {
}
