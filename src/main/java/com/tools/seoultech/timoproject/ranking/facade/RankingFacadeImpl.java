package com.tools.seoultech.timoproject.ranking.facade;

import com.tools.seoultech.timoproject.notification.NotificationRequest;
import com.tools.seoultech.timoproject.notification.NotificationService;
import com.tools.seoultech.timoproject.notification.NotificationType;
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
    private final NotificationService notificationService;
    private final RiotFacade riotFacade;
    
    @Override
    public void createRanking(Long memberId, String puuid) {
        RiotRankingDto riotRanking = riotFacade.getRiotRanking(puuid);
        rankingRedisService.createInitialRanking(memberId, riotRanking);
        notificationService.sendNotification(memberId,
            new NotificationRequest(NotificationType.RANKING_REGISTERED, "/ranking"));
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
    public List<RedisRankingInfo> getTopRankings(int limit) {
        return rankingRedisService.getTopRankings(limit);
    }

    @Override
    public List<RedisRankingInfo> getTopRankingsByUniversity(String university, int limit) {
        return rankingRedisService.getTopRankingsByUniversity(university, limit);
    }

    @Override
    public RedisRankingInfo getMyRankingInfo(Long memberId) {
        return rankingRedisService.getMyRankingInfo(memberId);
    }
}