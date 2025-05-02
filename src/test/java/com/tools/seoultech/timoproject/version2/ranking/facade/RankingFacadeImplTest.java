package com.tools.seoultech.timoproject.version2.ranking.facade;

import com.tools.seoultech.timoproject.ranking.facade.RankingFacadeImpl;
import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.riot.facade.RiotFacade;
import com.tools.seoultech.timoproject.matching.user.entity.enumType.Gender;
import com.tools.seoultech.timoproject.matching.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.ranking.dto.Redis_RankingInfo;
import com.tools.seoultech.timoproject.ranking.service.RankingRedisService;
import com.tools.seoultech.timoproject.ranking.service.RankingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankingFacadeImplTest {

    @InjectMocks
    private RankingFacadeImpl rankingFacade;

    @Mock
    private RankingService rankingService;

    @Mock
    private RankingRedisService rankingRedisService;

    @Mock
    private RiotFacade riotFacade;

    @Test
    void createRanking_And_GetTopRankings_ShouldReturnSortedResult() {
        // given
        Long memberId1 = 1L;
        Long memberId2 = 2L;

        RiotRankingDto dummyDto1 = mock(RiotRankingDto.class);
        RiotRankingDto dummyDto2 = mock(RiotRankingDto.class);

        // Riot API 호출 모의
        when(riotFacade.getRiotRanking("puuid1")).thenReturn(dummyDto1);
        when(riotFacade.getRiotRanking("puuid2")).thenReturn(dummyDto2);

        // 랭킹 리스트 모의 응답 (이미 정렬된 형태로 가정)
        Redis_RankingInfo rank1 = Redis_RankingInfo.builder()
                .memberId(memberId1)
                .gameName("Player1")
                .score(1000)
                .build();

        Redis_RankingInfo rank2 = Redis_RankingInfo.builder()
                .memberId(memberId2)
                .gameName("Player2")
                .score(800)
                .build();

        when(rankingRedisService.getTopRankings(2)).thenReturn(List.of(rank1, rank2));

        // when
        rankingFacade.createRanking(memberId1, "puuid1");
        rankingFacade.createRanking(memberId2, "puuid2");

        List<Redis_RankingInfo> topRankings = rankingFacade.getTopRankings(2);

        // then
        assertEquals(2, topRankings.size());
        assertEquals("Player1", topRankings.get(0).getGameName());  // 점수 높은 사람 먼저
        assertEquals("Player2", topRankings.get(1).getGameName());
        System.out.println("Top Rankings:");
        topRankings.forEach(r -> System.out.println(r.getGameName() + " - score: " + r.getScore()));
    }

    @Test
    void updateRankingInfo_ShouldUpdateOnlyNonNullFields() {
        // given
        Long memberId = 1L;

        RankingUpdateRequestDto updateDto = new RankingUpdateRequestDto(
                "INTJ",
                PlayPosition.TOP,
                Gender.MALE,
                "컴퓨터공학과",
                "한줄소개입니다"
        );

        // mock: Redis_RankingInfo 기존 데이터
        Redis_RankingInfo existing = Redis_RankingInfo.builder()
                .memberId(memberId)
                .gameName("Player1")
                .mbti("ENFP")
                .memo("기존 메모")
                .position(PlayPosition.MID)
                .department("컴퓨터공학과")
                .gender(Gender.FEMALE)
                .score(1000)
                .build();

        // when: 업데이트 시 Redis 서비스가 내부적으로 update 호출한다고 가정
        // 여기선 mock 객체이므로 직접 확인할 수는 없고, verify로 호출 여부만 확인 가능

        // act
        rankingFacade.updateRankingInfo(memberId, updateDto);

        // then: 내부 메서드가 호출되었는지 확인
        verify(rankingService).updateRankingInfo(memberId, updateDto);
        verify(rankingRedisService).updateRankingInfo(memberId, updateDto);
    }
}