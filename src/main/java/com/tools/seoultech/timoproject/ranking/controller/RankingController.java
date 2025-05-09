package com.tools.seoultech.timoproject.ranking.controller;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import com.tools.seoultech.timoproject.ranking.RedisDataSeeder;
import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.ranking.dto.Redis_RankingInfo;
import com.tools.seoultech.timoproject.ranking.facade.RankingFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
@Tag(name = "Ranking", description = "랭킹 API")
public class RankingController {

    private final RankingFacade rankingFacade;
    private final RedisDataSeeder redisDataSeeder;

    @Operation(summary = "랭킹 등록", description = "Puuid로 라이엇 API에서 데이터를 받아 랭킹 정보를 등록합니다.")
    @PostMapping("")
    public ResponseEntity<?> registerRanking(@CurrentMemberId Long memberId, @RequestParam String puuid) {
        rankingFacade.createRanking(memberId, puuid);
        return ResponseEntity.ok(APIDataResponse.of("ok"));
    }

    @Operation(summary = "내 랭킹 정보 조회", description = "현재 로그인한 사용자의 랭킹 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<APIDataResponse<Redis_RankingInfo>> getMyRankingInfo(@CurrentMemberId Long memberId) {
        Redis_RankingInfo myRankingInfo = rankingFacade.getMyRankingInfo(memberId);
        return ResponseEntity.ok(APIDataResponse.of(myRankingInfo));
    }

    @Operation(summary = "전체 상위 랭킹 조회", description = "전체 사용자 중 상위 랭킹 목록을 조회합니다. limit 파라미터로 개수를 제한할 수 있습니다.")
    @GetMapping("/top")
    public ResponseEntity<?> getTopRankingList(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(APIDataResponse.of(rankingFacade.getTopRankings(limit)));
    }

    @Operation(summary = "대학교별 상위 랭킹 조회", description = "특정 대학교의 상위 랭킹 목록을 조회합니다.")
    @GetMapping("/top/univ")
    public ResponseEntity<APIDataResponse<List<Redis_RankingInfo>>> getTopByUniversity(
            @RequestParam String university,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<Redis_RankingInfo> list = rankingFacade.getTopRankingsByUniversity(university, limit);
        return ResponseEntity.ok(APIDataResponse.of(list));
    }

    @Operation(summary = "랭킹 정보 업데이트", description = "사용자의 랭킹 정보를 업데이트합니다.")
    @PostMapping("/update")
    public ResponseEntity<?> updateRankingInfo(@CurrentMemberId Long memberId, @RequestBody RankingUpdateRequestDto dto){
        rankingFacade.updateRankingInfo(memberId, dto);
        return ResponseEntity.ok(APIDataResponse.of("ok"));
    }

    @Operation(summary = "[테스트용] Redis 시드 데이터 삽입", description = "Redis에 테스트용 랭킹 데이터를 시드로 삽입합니다.")
    @PostMapping("/seed")
    public ResponseEntity<?> seedAllRankings() {
        try {
            redisDataSeeder.seedAll();
            return ResponseEntity.ok(APIDataResponse.of("✅ Redis seeding complete"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(APIDataResponse.of("❌ Seeding failed: " + e.getMessage()));
        }
    }

    @Operation(summary = "[테스트용] Redis 랭킹 데이터 플러시", description = "Redis에서 랭킹 데이터를 모두 삭제합니다.")
    @DeleteMapping("/flush")
    public ResponseEntity<?> flushRedisRankings() {
        try {
            rankingFacade.flushAllRedisRankings();
            return ResponseEntity.ok(APIDataResponse.of("✅ Redis ranking data flushed"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(APIDataResponse.of("❌ Flush failed: " + e.getMessage()));
        }
    }

    @Operation(summary = "내 랭킹 삭제", description = "현재 로그인한 사용자의 랭킹 정보를 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<?> deleteMyRanking(@CurrentMemberId Long memberId) {
        rankingFacade.deleteRanking(memberId);
        return ResponseEntity.ok(APIDataResponse.of("ok"));
    }
}
