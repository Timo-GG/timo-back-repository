package com.tools.seoultech.timoproject.riot.dto;

import java.util.List;

public record RecentMatchFullResponse(
        String gameName,
        String tagLine,
        String profileIconUrl,
        RankInfoDto rankInfo,
        List<MatchSummaryDTO> matchSummaries
) {}
