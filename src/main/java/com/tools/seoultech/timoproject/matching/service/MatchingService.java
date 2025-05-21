package com.tools.seoultech.timoproject.matching.service;


import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.DuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.ScrimPage;
import com.tools.seoultech.timoproject.matching.service.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final MyPageService myPageService;
    private final BoardService boardService;

    private final MyPageMapper myPageMapper;

    /** Matching 수락 시  */
    public DuoPage doDuoAcceptEvent(UUID myPageUUID) throws Exception {
        UUID boardUUID = myPageService.readDuoMyPage(myPageUUID).getBoardUUID();
    // 1. MySQL 엔티티로 전환.
        // TODO: 채팅룸 연결.
        DuoPage entity = myPageService.createDuoPage(myPageUUID);
    // 2. Redis 엔티티 삭제.
        myPageService.deleteDuoMyPage(myPageUUID);
    // 3. DuoBoard 엔티티 삭제.
        boardService.deleteDuoBoardById(boardUUID);
        return entity;
    }

    public ScrimPage doScrimAcceptEvent(UUID myPageUUID) throws Exception {
        UUID boardUUID = myPageService.readScrimMyPage(myPageUUID).getBoardUUID();
    // 1. MySQL 엔티티로 전환.
        // TODO: 채팅룸 연결.
        ScrimPage entity = myPageService.createScrimPage(myPageUUID);
    // 2. Redis MyPage 엔티티 삭제.
        myPageService.deleteScrimMyPage(myPageUUID);
    // 3. ScrimBoard 엔티티 삭제.
        boardService.deleteScrimBoardById(boardUUID);
        return entity;
    }

    /** Matching 거절 시 */
    public void doDuoRejectEvent(UUID myPageUUID) throws Exception {
    // 1. DuoBoard 삭제 없이 Redis MyPage 엔티티만 삭제.
        myPageService.deleteDuoMyPage(myPageUUID);
    }
    public void doScrimRejectEvent(UUID myPageUUID) throws Exception {
    // 1. ScrimBoard 삭제 없이 Redis MyPage 엔티티만 삭제.
        myPageService.deleteScrimMyPage(myPageUUID);
    }
}
