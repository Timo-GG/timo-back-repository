package com.tools.seoultech.timoproject.matching.domain.myPage.repository.projections;


import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;


import java.util.List;
import java.util.UUID;

public interface ScrimMyPageOnly {
    UUID getMyPageUUID();
    List<RiotAccount> getAcceptorPartyInfo();
    List<RiotAccount> getRequestorPartyInfo();
}
