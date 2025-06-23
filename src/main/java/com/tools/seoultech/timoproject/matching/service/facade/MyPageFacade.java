package com.tools.seoultech.timoproject.matching.service.facade;

import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;

import java.util.List;
import java.util.UUID;

public interface MyPageFacade {
    /** Redis 마이페이지 */
    MatchingDTO.Response createMyPage(MatchingDTO.Request dto) throws Exception;
    MatchingDTO.Response readMyPage(UUID myPageUUID) throws Exception;
    List<MatchingDTO.Response> readAllMyPage(MatchingCategory matchingCategory) throws Exception;

    List<MatchingDTO.Response> readAllMyRequestor(Long requestorId) throws Exception;
    List<MatchingDTO.Response> readAllMyAcceptor(Long acceptorId) throws Exception;

    boolean existsPageBy(Long memberId, UUID boardUUID);

    void deleteMyPage(UUID myPageUUID) throws Exception;
    void deleteAllMyPage(MatchingCategory matchingCategory) throws Exception;

    /** MySQL 평가하기 마이페이지 */
    MyPageDTO.Response readMyPage(Long mypageId) throws Exception;
    List<MyPageDTO.ResponseMyPage> readMyPageByMemberId(Long memberId) throws Exception;

    /** 백엔드 내부 테스트용 MySQL CRUD */
    MyPage createPage(UUID mypageUUID) throws Exception;
    List<MyPageDTO.Response> readAllPage() throws Exception;
    void deleteAllPage() throws Exception;

    void delete(Long mypageId);
}
