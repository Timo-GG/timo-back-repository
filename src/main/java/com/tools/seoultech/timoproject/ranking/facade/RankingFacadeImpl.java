package com.tools.seoultech.timoproject.ranking.facade;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.member.MemberRepository;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.notification.service.AsyncNotificationService;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;
import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.riot.facade.RiotFacade;
import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.ranking.dto.RedisRankingInfo;
import com.tools.seoultech.timoproject.ranking.service.RankingRedisService;
import com.tools.seoultech.timoproject.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankingFacadeImpl implements RankingFacade {
    private final RankingService rankingService;
    private final RankingRedisService rankingRedisService;
    private final AsyncNotificationService asyncNotificationService;
    private final MemberRepository memberRepository;
    private final RiotFacade riotFacade;

    @Override
    public void createRanking(Long memberId, String puuid) {
        RiotRankingDto riotRanking = riotFacade.getRiotRanking(puuid);
        rankingRedisService.createInitialRanking(memberId, riotRanking);

        asyncNotificationService.sendRankingNotificationAsync(
                memberId, NotificationType.RANKING_REGISTERED, "/ranking");
    }

    @Override
    public void updateRankingInfo(Long memberId, RankingUpdateRequestDto dto) {
        rankingService.updateRankingInfo(memberId, dto);
        rankingRedisService.updateRankingInfo(memberId, dto);
    }

    @Override
    public void flushAllRedisRankings() {
        rankingRedisService.flushAllRankingData();
    }


    @Override
    public void deleteRanking(Long memberId) {
        rankingRedisService.deleteRankingByMemberId(memberId);
    }

    @Override
    public List<RedisRankingInfo> getTopRankings(int offset, int limit) {
        return rankingRedisService.getTopRankings(offset, limit);
    }

    @Override
    public List<RedisRankingInfo> getTopRankingsByUniversity(String university, int offset, int limit) {
        return rankingRedisService.getTopRankingsByUniversity(university, offset, limit);
    }

    @Override
    public RedisRankingInfo getMyRankingInfo(Long memberId) {
        return rankingRedisService.getMyRankingInfo(memberId);
    }

    @Override
    public long getTotalRankingCount() {
        return rankingRedisService.getTotalRankingCount();
    }

    @Override
    public long getTotalRankingCountByUniversity(String university) {
        return rankingRedisService.getTotalRankingCountByUniversity(university);
    }

    @Override
    public int getRankingPosition(String name, String tag) {
        return rankingRedisService.getRankingPosition(name, tag);
    }

    @Override
    public void updateVerificationType(Long memberId, String verificationType) {
        rankingRedisService.updateVerificationType(memberId, verificationType);
    }

    @Override
    public void updateRankingFromRiotAPI(Long memberId) {
        try {
            // 1. 회원 정보 조회
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

            String puuid = member.getRiotAccount().getPuuid();

            // 2. 기존 랭킹 정보 조회
            RedisRankingInfo oldInfo = rankingRedisService.getMyRankingInfo(memberId);

            // 3. Riot API에서 최신 정보 조회
            RiotRankingDto riotRankingDto = riotFacade.getRiotRanking(puuid);

            // 4. 랭킹 정보 업데이트
            rankingRedisService.updateRankingFromRiotAPI(memberId, riotRankingDto);

            // 5. 업데이트된 정보 조회
            RedisRankingInfo newInfo = rankingRedisService.getMyRankingInfo(memberId);

            // 6. 변경사항 확인 및 알림 발송

            log.info("회원 랭킹 업데이트 완료: memberId={}, oldScore={}, newScore={}",
                    memberId, oldInfo.getScore(), newInfo.getScore());

        } catch (Exception e) {
            log.error("회원 랭킹 업데이트 실패: memberId={}", memberId, e);
            throw new RuntimeException("랭킹 업데이트 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateAllRankingsFromRiotAPI() {
        try {
            List<Member> members = memberRepository.findAllWithRiotAccount();
            log.info("전체 랭킹 업데이트 시작: 대상 회원 수 = {}", members.size());

            int successCount = 0;
            int failCount = 0;

            // 배치 처리 (100명씩)
            int batchSize = 100;
            for (int i = 0; i < members.size(); i += batchSize) {
                int endIndex = Math.min(i + batchSize, members.size());
                List<Member> batch = members.subList(i, endIndex);

                log.info("배치 처리 중: {}/{}", endIndex, members.size());

                for (Member member : batch) {
                    try {
                        updateRankingFromRiotAPI(member.getMemberId());
                        successCount++;
                    } catch (Exception e) {
                        log.warn("회원 랭킹 업데이트 실패: memberId={}, error={}",
                                member.getMemberId(), e.getMessage());
                        failCount++;
                    }
                }

                // API 호출 제한을 위한 딜레이
                if (i + batchSize < members.size()) {
                    Thread.sleep(2000); // 2초 대기
                }
            }

            log.info("전체 랭킹 업데이트 완료: 성공={}, 실패={}", successCount, failCount);

        } catch (Exception e) {
            log.error("전체 랭킹 업데이트 중 오류 발생", e);
            throw new RuntimeException("전체 랭킹 업데이트 실패: " + e.getMessage(), e);
        }
    }
}