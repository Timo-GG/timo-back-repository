package com.tools.seoultech.timoproject.matching.service;


import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.DuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.ScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisDuoPageOnly;
import com.tools.seoultech.timoproject.matching.service.mapper.MyPageMapper;
import com.tools.seoultech.timoproject.notification.NotificationRequest;
import com.tools.seoultech.timoproject.notification.NotificationService;
import com.tools.seoultech.timoproject.notification.NotificationType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingService {
    private final MyPageService myPageService;
    private final BoardService boardService;
    private final MyPageMapper myPageMapper;

    /** Duo 매칭 수락 - 핵심 로직만 */
    public DuoPage doDuoAcceptEvent(UUID myPageUUID, UUID boardUUID) throws Exception {
        // 1. MySQL 엔티티로 전환
        DuoPage entity = myPageService.createDuoPage(myPageUUID);

        // 2. Redis 엔티티 삭제
        myPageService.deleteDuoMyPage(myPageUUID);

        // 3. DuoBoard 엔티티 삭제 (미리 받은 boardUUID 사용)
        boardService.deleteDuoBoardById(boardUUID);

        return entity;
    }

    public ScrimPage doScrimAcceptEvent(UUID myPageUUID, UUID boardUUID) throws Exception {
        // 1. MySQL 엔티티로 전환
        ScrimPage entity = myPageService.createScrimPage(myPageUUID);

        // 2. Redis MyPage 엔티티 삭제
        myPageService.deleteScrimMyPage(myPageUUID);

        // 3. ScrimBoard 엔티티 삭제 (미리 받은 boardUUID 사용)
        boardService.deleteScrimBoardById(boardUUID);

        return entity;
    }

    /** Duo 매칭 거절 */
    public void doDuoRejectEvent(UUID myPageUUID) throws Exception {
        myPageService.deleteDuoMyPage(myPageUUID);
    }

    /** Scrim 매칭 거절 */
    public void doScrimRejectEvent(UUID myPageUUID) throws Exception {
        myPageService.deleteScrimMyPage(myPageUUID);
    }
}
