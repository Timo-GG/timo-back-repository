package com.tools.seoultech.timoproject.member.facade;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.matching.service.BoardService;
import com.tools.seoultech.timoproject.matching.service.MyPageService;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.RiotVerificationType;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.ranking.facade.RankingFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerificationSyncFacade {
    private final RankingFacade rankingFacade;
    private final MyPageService myPageService;
    private final BoardService boardService;
    private final MemberService memberService;

    /**
     * RSO 인증 완료 후 모든 시스템 동기화
     * - 역할: 인증 타입 변경의 통합 관리자
     * - 책임: Member, Ranking, MyPage 도메인 간 데이터 일관성 보장
     * - 협력: 각 도메인 서비스와 협력하여 순차적 업데이트 수행
     */
    @Transactional
    public void syncVerificationTypeAcrossAllSystems(Long memberId, String verificationType) {
        try {
            validateAndUpdateMemberVerification(memberId, verificationType);
            syncRankingVerification(memberId, verificationType);
            syncBoardVerification(memberId, verificationType);
            syncMyPageVerification(memberId, verificationType);

            log.info("✅ 전체 시스템 인증 타입 동기화 완료: memberId={}", memberId);
        } catch (Exception e) {
            log.error("❌ 전체 시스템 인증 타입 동기화 실패: memberId={}", memberId, e);
            throw new BusinessException(e, ErrorCode.VERIFICATION_SYNC_FAILED);
        }
    }

    private void validateAndUpdateMemberVerification(Long memberId, String verificationType) {
        Member member = memberService.getById(memberId);

        // 유효한 인증 타입인지 검증
        try {
            RiotVerificationType.valueOf(verificationType);
        } catch (IllegalArgumentException e) {
            log.error("❌ 유효하지 않은 인증 타입: {}", verificationType);
            throw new BusinessException(ErrorCode.INVALID_VERIFICATION_TYPE);
        }

        // RiotAccount 존재 여부 검증
        if (member.getRiotAccount() == null) {
            log.error("❌ RiotAccount가 없는 회원에 대한 인증 타입 업데이트 시도: memberId={}", memberId);
            throw new BusinessException(ErrorCode.RIOT_ACCOUNT_NOT_FOUND);
        }

        // Member 서비스를 통해 인증 타입 업데이트
        memberService.updateVerificationType(memberId, verificationType);

        log.debug("✅ Member 인증 타입 검증 및 업데이트 완료: memberId={}, type={}",
                memberId, verificationType);
    }

    private void syncRankingVerification(Long memberId, String verificationType) {
        try {
            rankingFacade.updateVerificationType(memberId, verificationType);
        } catch (BusinessException e) {
            if (e.getErrorCode() == ErrorCode.REDIS_RANKING_NOT_FOUND) {
                log.info("ℹ️ 랭킹 정보 없음 - 건너뛰기: memberId={}", memberId);
            } else {
                throw e;
            }
        }
    }

    private void syncBoardVerification(Long memberId, String verificationType) {
        try {
            boardService.updateVerificationTypeInBoards(memberId, verificationType);
        } catch (Exception e) {
            log.warn("⚠️ Board 인증 타입 동기화 실패 (무시하고 계속): memberId={}", memberId, e);
            // Board는 임시 데이터이므로 실패해도 전체 프로세스를 중단하지 않음
        }
    }

    private void syncMyPageVerification(Long memberId, String verificationType) {
        try {
            myPageService.updateVerificationTypeInMyPages(memberId, verificationType);
        } catch (Exception e) {
            log.warn("⚠️ MyPage 인증 타입 동기화 실패 (무시하고 계속): memberId={}", memberId, e);
            // MyPage는 임시 데이터이므로 실패해도 전체 프로세스를 중단하지 않음
        }
    }



}
