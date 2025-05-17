package com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType;

import com.tools.seoultech.timoproject.riot.dto.MatchSummaryDTO;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompactPlayerHistory {
    private RankInfoDto rankInfo;
    private List<String> most3Champ;
    private List<MatchSummaryDTO> last10Match;
}
