package com.tools.seoultech.timoproject.matching.domain.board.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;
import java.util.UUID;

public interface ScrimBoardOnly {
    UUID getBoardUUID();
    Long getMemberId();
    ColosseumMapCode getMapCode();
    String getMemo();
    Integer getHeadCount();
    List<RiotAccount> getPartyInfo();
    CompactPlayerHistory getCompactPlayerHistory();
}
