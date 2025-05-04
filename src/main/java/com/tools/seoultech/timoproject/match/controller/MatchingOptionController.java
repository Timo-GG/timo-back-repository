package com.tools.seoultech.timoproject.match.controller;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionResponse;
import com.tools.seoultech.timoproject.match.service.MatchingOptionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/matching-options")
@RequiredArgsConstructor
public class MatchingOptionController {

    private final MatchingOptionServiceImpl matchingOptionService;

    @PostMapping()
    public ResponseEntity<MatchingOptionResponse> createMatchingOption(
            @CurrentMemberId Long memberId,
            @RequestBody MatchingOptionRequest request) {
        MatchingOptionResponse response = matchingOptionService.updateMatchingOption(memberId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<MatchingOptionResponse> getMatchingOption(@CurrentMemberId Long memberId) {
        MatchingOptionResponse response = matchingOptionService.getMatchingOption(memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping()
    public ResponseEntity<MatchingOptionResponse> updateMatchingOption(
            @CurrentMemberId Long memberId,
            @RequestBody MatchingOptionRequest request) {
        MatchingOptionResponse response = matchingOptionService.updateMatchingOption(memberId, request);
        return ResponseEntity.ok(response);
    }
}