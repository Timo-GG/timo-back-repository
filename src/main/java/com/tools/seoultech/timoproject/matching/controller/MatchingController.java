package com.tools.seoultech.timoproject.matching.controller;

import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.DuoPage;
import com.tools.seoultech.timoproject.matching.service.MatchingService;
import com.tools.seoultech.timoproject.matching.service.MyPageService;
import com.tools.seoultech.timoproject.matching.service.mapper.MyPageMapper;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
@Tag(name = "Matching", description = "Matching API")
public class MatchingController {
    private final MatchingService matchingService;
    private final MyPageService myPageService;
    private final MyPageMapper myPageMapper;

    @PostMapping("/myPage/duo")
    public ResponseEntity<APIDataResponse<?>> testRedis(@RequestBody MatchingDTO.RequestDuo dto) throws Exception {
        var redis = myPageService.createDuoMyPage(dto);
        return ResponseEntity.ok(APIDataResponse.of(redis));
    }

    @GetMapping("/testMySQL/{myPageUUID}")
    public ResponseEntity<APIDataResponse<DuoPage>> doAcceptSequence(@PathVariable UUID myPageUUID) throws Exception {
        var entity = matchingService.doDuoAcceptEvent(myPageUUID);
        return ResponseEntity.ok(APIDataResponse.of(entity));
    }
}
