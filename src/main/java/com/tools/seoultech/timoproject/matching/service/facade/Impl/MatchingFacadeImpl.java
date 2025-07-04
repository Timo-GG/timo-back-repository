package com.tools.seoultech.timoproject.matching.service.facade.Impl;

import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.DuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.ScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisDuoPageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisScrimPageOnly;
import com.tools.seoultech.timoproject.matching.service.MatchingService;
import com.tools.seoultech.timoproject.matching.service.MyPageService;
import com.tools.seoultech.timoproject.matching.service.facade.MatchingFacade;
import com.tools.seoultech.timoproject.matching.service.facade.MyPageFacade;
import com.tools.seoultech.timoproject.matching.service.mapper.MyPageMapper;
import com.tools.seoultech.timoproject.notification.dto.NotificationRequest;
import com.tools.seoultech.timoproject.notification.service.AsyncNotificationService;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;
import com.tools.seoultech.timoproject.riot.utils.RiotAccountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchingFacadeImpl implements MatchingFacade {
    private final MatchingService matchingService;
    private final MyPageService myPageService;
    private final MyPageFacade myPageFacade;
    private final MyPageMapper myPageMapper;
    private final ChatService chatService;
    private final AsyncNotificationService asyncNotificationService;

    private static final String CHAT_URL_PREFIX = "/chat?tab=chat";
    private static final String MYPAGE_URL = "/mypage";

    @Override
    public MyPageDTO.Response doAcceptEvent(UUID myPageUUID) throws Exception {
        MatchingDTO.Response dto = myPageFacade.readMyPage(myPageUUID);

        if(dto instanceof MatchingDTO.ResponseDuo) {
            RedisDuoPageOnly duoPageData = myPageService.readDuoMyPage(myPageUUID);
            Long roomId = processMatchSuccess(duoPageData);

            DuoPage entity = matchingService.doDuoAcceptEvent(myPageUUID, duoPageData.getBoardUUID());
            return myPageMapper.toDuoDto(entity, roomId);

        } else if (dto instanceof MatchingDTO.ResponseScrim) {
            RedisScrimPageOnly scrimPageData = myPageService.readScrimMyPage(myPageUUID);
            Long roomId = processMatchSuccess(scrimPageData);

            ScrimPage entity = matchingService.doScrimAcceptEvent(myPageUUID, scrimPageData.getBoardUUID());
            return myPageMapper.toScrimDto(entity, roomId);
        }

        throw new GeneralException("Matching 로직 내부에서 실패했습니다.");
    }

    @Override
    public void doRejectEvent(UUID myPageUUID) throws Exception {
        MatchingDTO.Response dto = myPageFacade.readMyPage(myPageUUID);

        if(dto instanceof MatchingDTO.ResponseDuo) {
            RedisDuoPageOnly duoPageData = myPageService.readDuoMyPage(myPageUUID);
            matchingService.deleteDuoMyPage(myPageUUID);
            processMatchReject(duoPageData);

        } else if (dto instanceof MatchingDTO.ResponseScrim) {
            RedisScrimPageOnly scrimPageData = myPageService.readScrimMyPage(myPageUUID);
            matchingService.deleteScrimMyPage(myPageUUID);
            processMatchReject(scrimPageData);

        } else {
            throw new GeneralException("Matching 로직 내부에서 실패했습니다.");
        }
    }

    @Override
    public void doCancelEvent(UUID myPageUUID) throws Exception {
        MatchingDTO.Response dto = myPageFacade.readMyPage(myPageUUID);
        if(dto instanceof MatchingDTO.ResponseDuo) {
            matchingService.deleteDuoMyPage(myPageUUID);
            log.info("Duo 매칭 취소됨. UUID={}", myPageUUID);

        } else if (dto instanceof MatchingDTO.ResponseScrim) {
            matchingService.deleteScrimMyPage(myPageUUID);
            log.info("Scrim 매칭 취소됨. UUID={}", myPageUUID);

        } else {
            throw new GeneralException("Matching 로직 내부에서 실패했습니다.");
        }
    }

    // === Private Helper Methods ===

    /**
     * 매칭 성공 후처리 - DuoPage용
     */
    private Long processMatchSuccess(RedisDuoPageOnly duoPageData) {
        String acceptorName = RiotAccountUtil.extractGameName(
                duoPageData.getAcceptorCertifiedMemberInfo().getRiotAccount());

        return processMatchSuccessCommon(
                duoPageData.getBoardUUID(),
                duoPageData.getAcceptorId(),
                duoPageData.getRequestorId(),
                NotificationType.DUO_ACCEPTED,
                "DUO",
                acceptorName
        );
    }

    /**
     * 매칭 성공 후처리 - ScrimPage용
     */
    private Long processMatchSuccess(RedisScrimPageOnly scrimPageData) {
        String acceptorName = RiotAccountUtil.extractGameName(
                scrimPageData.getAcceptorCertifiedMemberInfo().getRiotAccount());

        return processMatchSuccessCommon(
                scrimPageData.getBoardUUID(),
                scrimPageData.getAcceptorId(),
                scrimPageData.getRequestorId(),
                NotificationType.SCRIM_ACCEPTED,
                "SCRIM",
                acceptorName
        );
    }

    /**
     * 매칭 거절 후처리 - DuoPage용
     */
    private void processMatchReject(RedisDuoPageOnly duoPageData) {
        String acceptorName = RiotAccountUtil.extractGameName(
                duoPageData.getAcceptorCertifiedMemberInfo().getRiotAccount());

        processMatchRejectCommon(
                duoPageData.getRequestorId(),
                NotificationType.DUO_REJECTED,
                "DUO",
                acceptorName  // requestor에게 acceptor 닉네임 전송
        );
    }

    /**
     * 매칭 거절 후처리 - ScrimPage용
     */
    private void processMatchReject(RedisScrimPageOnly scrimPageData) {
        String acceptorName = RiotAccountUtil.extractGameName(
                scrimPageData.getAcceptorCertifiedMemberInfo().getRiotAccount());

        processMatchRejectCommon(
                scrimPageData.getRequestorId(),
                NotificationType.SCRIM_REJECTED,
                "SCRIM",
                acceptorName  // requestor에게 acceptor 닉네임 전송
        );
    }

    /**
     * 매칭 성공 공통 처리 로직
     */
    private Long processMatchSuccessCommon(UUID boardUUID, Long acceptorId, Long requestorId,
                                           NotificationType notificationType, String matchType,
                                           String acceptorName) {
        try {
            // 채팅방 생성 (동기)
            Long chatRoomId = chatService.createChatRoomForMatch(
                    boardUUID.toString(), acceptorId, requestorId);

            // 🔥 비동기 알림 전송
            String redirectUrl = CHAT_URL_PREFIX + chatRoomId;
            asyncNotificationService.sendMatchingResultNotificationAsync(
                    requestorId, acceptorName, notificationType, redirectUrl);

            log.info("매칭 성공 후처리 완료. type={}, chatRoomId={}, acceptor={}",
                    matchType, chatRoomId, acceptorName);

            return chatRoomId;

        } catch (Exception e) {
            log.error("매칭 성공 후처리 실패. matchType={}", matchType, e);
            throw new GeneralException("매칭 후처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 매칭 거절 공통 처리 로직
     */
    private void processMatchRejectCommon(Long requestorId, NotificationType notificationType,
                                          String matchType, String acceptorName) {
        try {
            // 🔥 비동기 알림 전송
            asyncNotificationService.sendMatchingResultNotificationAsync(
                    requestorId, acceptorName, notificationType, MYPAGE_URL);

            log.info("매칭 거절 후처리 완료. type={}, acceptor={}", matchType, acceptorName);

        } catch (Exception e) {
            log.error("매칭 거절 후처리 실패. matchType={}", matchType, e);
            throw new GeneralException("매칭 거절 처리 중 오류가 발생했습니다.");
        }
    }
}
