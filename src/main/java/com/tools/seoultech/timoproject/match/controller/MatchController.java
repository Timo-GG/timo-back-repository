package com.tools.seoultech.timoproject.match.controller;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;
import com.tools.seoultech.timoproject.match.service.MatchingService;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/match")
public class MatchController {

    private final MatchingService matchingService;

    /** 매칭 시작 */
    @PostMapping("/queue")
    public APIDataResponse<?> startMatch(@CurrentMemberId Long memberId, @RequestBody MatchingOptionRequest request) {

        log.info("매칭 요청 받음. memberId: {}, request: {}", memberId, request);

        Optional<String> matchId = matchingService.startMatch(memberId, request);

        if (matchId.isPresent()) {
            return APIDataResponse.of("매칭 요청이 즉시 처리되었습니다 : " + matchId.get());
        } else {
            return APIDataResponse.of("대기열에 등록되었습니다. 매칭 상대를 기다리는 중입니다.");
        }
    }

    /** 테스트 데이터 Redis 삽입 */
    @PostMapping("/queue/test")
    public APIDataResponse<?> saveTestDataToRedis() {
        matchingService.saveTestDataToRedis();
        return APIDataResponse.of("테스트 데이터가 Redis에 저장되었습니다.");
    }

    /** 매칭 취소 */
    @DeleteMapping("/cancel")
    public APIDataResponse<?> cancelMatch(@CurrentMemberId Long memberId) {
        matchingService.removeFromQueue(memberId);
        return APIDataResponse.of("매칭이 취소되었습니다.");
    }

    /** 모든 매칭 취소 */
    @DeleteMapping("/cancelAll/{gameMode}")
    public APIDataResponse<?> cancelAllMatch(@PathVariable String gameMode) {
        matchingService.removeAllFromQueue(gameMode);
        return APIDataResponse.of("모든 매칭이 취소되었습니다.");
    }

    /** 매칭 수락 */
//    @PostMapping("/accept/{matchId}")
//    public APIDataResponse<?> acceptMatch(@CurrentMemberId Long memberId, @PathVariable String matchId) {
//        boolean isMatchConfirmed = matchingService.acceptMatch(matchId, memberId);
//
//        if (isMatchConfirmed) {
//            return APIDataResponse.of("매칭이 확정되었습니다. 채팅방이 생성되었습니다.");
//        } else {
//            return APIDataResponse.of("매칭이 진행 중이거나 상대방의 응답을 기다리고 있습니다.");
//        }
//    }

    /** 매칭 거절 */
    @PostMapping("/deny/{matchId}")
    public APIDataResponse<?> denyMatch(@CurrentMemberId Long memberId, @PathVariable String matchId) {
        boolean isDenied = matchingService.denyMatch(matchId, memberId);

        if (isDenied) {
            return APIDataResponse.of("매칭이 거절되었습니다. 대기열에서 다시 매칭을 시도합니다.");
        } else {
            return APIDataResponse.of("매칭을 거절할 수 없습니다. 이미 취소되었거나 오류가 발생했습니다.");
        }
    }

    /** 대기 중인 유저 조회 */
    @GetMapping("/waiting/{gameMode}")
    public APIDataResponse<?> getWaitingUsers(@PathVariable String gameMode) {
        List<Long> waitingUsers = matchingService.getWaitingUsers(gameMode);

        if (waitingUsers.isEmpty()) {
            return APIDataResponse.of("현재 대기 중인 유저가 없습니다.");
        } else {
            return APIDataResponse.of("대기 중인 유저 리스트 : " + waitingUsers);
        }
    }

    /** 매칭 상태 조회 */
    @GetMapping("/status")
    public APIDataResponse<?> getMatchStatus(@CurrentMemberId Long memberId) {
        return APIDataResponse.of(matchingService.getMatchStatus(memberId));
    }
}