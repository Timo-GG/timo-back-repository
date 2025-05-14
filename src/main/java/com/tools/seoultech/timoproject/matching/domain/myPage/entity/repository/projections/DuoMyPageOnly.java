package com.tools.seoultech.timoproject.matching.domain.myPage.entity.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

public interface DuoMyPageOnly {
    UUID getMyPageUUID();

    UserInfo getAcceptorUserInfo();
    DuoInfo getAcceptorDuoInfo();
    CompactPlayerHistory getAcceptorCompactPlayerHistory();

    UserInfo getRequestorUserInfo();
    DuoInfo getRequestorDuoInfo();
    CompactPlayerHistory getRequestorCompactPlayerHistory();

}
