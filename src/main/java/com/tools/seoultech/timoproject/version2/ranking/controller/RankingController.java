package com.tools.seoultech.timoproject.version2.ranking.controller;


import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.member.facade.MemberFacade;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import com.tools.seoultech.timoproject.version2.ranking.RedisDataSeeder;
import com.tools.seoultech.timoproject.version2.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.version2.ranking.dto.Redis_RankingInfo;
import com.tools.seoultech.timoproject.version2.ranking.facade.RankingFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
@Tag(name = "Ranking", description = "Ranking API")
public class RankingController {

    private final RankingFacade rankingFacade;
    private final RedisDataSeeder redisDataSeeder;   // 시더 주입


    @PostMapping("")
    public ResponseEntity<?> registerRanking(@CurrentMemberId Long memberId, @RequestParam String puuid) {
        rankingFacade.createRanking(memberId, puuid);
        return ResponseEntity.ok(APIDataResponse.of("ok"));
    }

    @GetMapping("/me")
    public ResponseEntity<APIDataResponse<Redis_RankingInfo>> getMyRankingInfo(@CurrentMemberId Long memberId) {
        Redis_RankingInfo myRankingInfo = rankingFacade.getMyRankingInfo(memberId);
        return ResponseEntity.ok(APIDataResponse.of(myRankingInfo));
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopRankingList(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(APIDataResponse.of(rankingFacade.getTopRankings(limit)));
    }

    @GetMapping("/top/univ")
    public ResponseEntity<APIDataResponse<List<Redis_RankingInfo>>> getTopByUniversity(
            @RequestParam String university,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<Redis_RankingInfo> list = rankingFacade.getTopRankingsByUniversity(university, limit);
        return ResponseEntity.ok(APIDataResponse.of(list));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateRankingInfo(@CurrentMemberId Long memberId, @RequestBody RankingUpdateRequestDto dto){
        rankingFacade.updateRankingInfo(memberId, dto);
        return ResponseEntity.ok(APIDataResponse.of("ok"));
    }

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

    @DeleteMapping("/flush")
    public ResponseEntity<?> flushRedisRankings() {
        try {
            rankingFacade.flushAllRedisRankings();
            return ResponseEntity.ok(APIDataResponse.of("✅ Redis ranking data flushed"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(APIDataResponse.of("❌ Flush failed: " + e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMyRanking(@CurrentMemberId Long memberId) {
        rankingFacade.deleteRanking(memberId);
        return ResponseEntity.ok(APIDataResponse.of("ok"));
    }
}
