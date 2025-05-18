package com.tools.seoultech.timoproject.ranking.controller;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import com.tools.seoultech.timoproject.ranking.RedisDataSeeder;
import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.ranking.dto.RedisRankingInfo;
import com.tools.seoultech.timoproject.ranking.facade.RankingFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<APIDataResponse<RedisRankingInfo>> getMyRankingInfo(@CurrentMemberId Long memberId) {
        RedisRankingInfo myRankingInfo = rankingFacade.getMyRankingInfo(memberId);
        return ResponseEntity.ok(APIDataResponse.of(myRankingInfo));
    }

    @GetMapping("/top")
    public ResponseEntity<APIDataResponse<Map<String, Object>>> getTopRankingList(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {

        List<RedisRankingInfo> list = rankingFacade.getTopRankings(offset, limit);
        long totalCount = rankingFacade.getTotalRankingCount();

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);

        return ResponseEntity.ok(APIDataResponse.of(result));
    }

    @GetMapping("/top/univ")
    public ResponseEntity<APIDataResponse<Map<String, Object>>> getTopByUniversity(
            @RequestParam String university,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {

        List<RedisRankingInfo> list = rankingFacade.getTopRankingsByUniversity(university, offset, limit);
        long totalCount = rankingFacade.getTotalRankingCountByUniversity(university);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);

        return ResponseEntity.ok(APIDataResponse.of(result));
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

    @Operation(summary = "소환사 순위 조회", description = "소환사명과 태그로 전체 순위(1부터 시작)를 조회합니다.")
    @GetMapping("/position")
    public ResponseEntity<APIDataResponse<Integer>> getRankingPosition(
            @RequestParam String name,
            @RequestParam String tag) {
        int rank = rankingFacade.getRankingPosition(name, tag); // 1부터 시작
        return ResponseEntity.ok(APIDataResponse.of(rank));
    }
}
