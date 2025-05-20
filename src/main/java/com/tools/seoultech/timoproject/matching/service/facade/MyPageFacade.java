package com.tools.seoultech.timoproject.matching.service.facade;

import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;

import java.util.List;
import java.util.UUID;

public interface MyPageFacade {
    public MatchingDTO.Response createMyPage(MatchingDTO.Request dto) throws Exception;
    public MatchingDTO.Response readMyPage(UUID myPageUUID) throws Exception;
    public List<MatchingDTO.Response> readAllMyPage(MatchingCategory matchingCategory) throws Exception;
    void deleteMyPage(UUID myPageUUID) throws Exception;
    void deleteAllMyPage(MatchingCategory matchingCategory) throws Exception;
}
