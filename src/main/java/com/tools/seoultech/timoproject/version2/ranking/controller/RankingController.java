package com.tools.seoultech.timoproject.version2.ranking.controller;


import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.member.facade.MemberFacade;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import com.tools.seoultech.timoproject.version2.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.version2.ranking.dto.Redis_RankingInfo;
import com.tools.seoultech.timoproject.version2.ranking.facade.RankingFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
@Tag(name = "Ranking", description = "Ranking API")
public class RankingController {

    private final RankingFacade rankingFacade;

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

    @PostMapping("/update")
    public ResponseEntity<?> updateRankingInfo(@CurrentMemberId Long memberId, @RequestBody RankingUpdateRequestDto dto){
        rankingFacade.updateRankingInfo(memberId, dto);
        return ResponseEntity.ok(APIDataResponse.of("ok"));
    }
}
