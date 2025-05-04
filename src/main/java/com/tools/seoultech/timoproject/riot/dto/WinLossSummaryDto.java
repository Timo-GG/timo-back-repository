package com.tools.seoultech.timoproject.riot.dto;

import lombok.Builder;

@Builder
public record WinLossSummaryDto(int wins, int losses) {
    public static WinLossSummaryDto of(int wins, int losses) {
        return new WinLossSummaryDto(wins, losses);
    }

    public static WinLossSummaryDto of(WinLossSummaryDto winLossSummaryDto) {
        return new WinLossSummaryDto(winLossSummaryDto.wins(), winLossSummaryDto.losses());
    }
}
