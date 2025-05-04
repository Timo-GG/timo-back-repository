package com.tools.seoultech.timoproject.rating.dto;

public record DuoResponse(
        Long id,
        Long duoId,
        int duoProfileImage,
        String playerName,
        String playerTag,
        String matchId,
        boolean isRated
) {
}
