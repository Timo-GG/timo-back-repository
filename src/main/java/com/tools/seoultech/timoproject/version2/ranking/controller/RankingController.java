package com.tools.seoultech.timoproject.version2.ranking.controller;


import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.member.facade.MemberFacade;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import com.tools.seoultech.timoproject.version2.ranking.facade.RankingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingFacade rankingFacade;

    @PostMapping("")
    public ResponseEntity<?> registerRanking(@CurrentMemberId Long memberId, @RequestParam String puuid) {
        rankingFacade.createRanking(memberId, puuid);
        return ResponseEntity.ok(APIDataResponse.of("ok"));
    }
}
