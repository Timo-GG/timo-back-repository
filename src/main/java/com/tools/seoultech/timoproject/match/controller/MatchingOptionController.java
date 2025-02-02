package com.tools.seoultech.timoproject.match.controller;

import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionResponse;
import com.tools.seoultech.timoproject.match.service.MatchingOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matching-options")
@RequiredArgsConstructor
public class MatchingOptionController {

    private final MatchingOptionService matchingOptionService;

    @PostMapping("/{memberId}")
    public ResponseEntity<MatchingOptionResponse> createMatchingOption(
            @PathVariable Long memberId,
            @RequestBody MatchingOptionRequest request) {
        MatchingOptionResponse response = matchingOptionService.updateMatchingOption(memberId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MatchingOptionResponse> getMatchingOption(@PathVariable Long memberId) {
        MatchingOptionResponse response = matchingOptionService.getMatchingOption(memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MatchingOptionResponse> updateMatchingOption(
            @PathVariable Long memberId,
            @RequestBody MatchingOptionRequest request) {
        MatchingOptionResponse response = matchingOptionService.updateMatchingOption(memberId, request);
        return ResponseEntity.ok(response);
    }
}