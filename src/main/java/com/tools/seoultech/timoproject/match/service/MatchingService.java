package com.tools.seoultech.timoproject.match.service;

import com.tools.seoultech.timoproject.match.dto.MatchResult;
import com.tools.seoultech.timoproject.match.dto.MatchStatusResponse;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MatchingService {

    List<Long> getWaitingUsers(String gameMode);

    Optional<String> startMatch(Long memberId, MatchingOptionRequest request);

    Optional<String> findMatch(Long memberId);

    void removeFromQueue(Long memberId);

    void removeAllFromQueue(String gameMode);

    void saveTestDataToRedis();

    MatchResult acceptMatch(String matchId, Long memberId);

    boolean denyMatch(String matchId, Long memberId);

    Set<Long> getMatchMemberIds(String matchId);

    MatchStatusResponse getMatchStatus(Long memberId);
}