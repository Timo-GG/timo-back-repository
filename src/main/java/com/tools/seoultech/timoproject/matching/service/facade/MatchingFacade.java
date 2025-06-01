package com.tools.seoultech.timoproject.matching.service.facade;

import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;

import java.util.UUID;

public interface MatchingFacade {
    Long doAcceptEvent(UUID myPageUUID) throws Exception;
    void doRejectEvent(UUID myPageUUID) throws Exception;
}
