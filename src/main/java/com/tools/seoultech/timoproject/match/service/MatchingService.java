package com.tools.seoultech.timoproject.match.service;

import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;

import java.util.List;
import java.util.Optional;

public interface MatchingService {

    List<Long> getWaitingUsers(String gameMode);

    Optional<String> startMatch(Long memberId, MatchingOptionRequest request);

    Optional<String> findMatch(Long memberId);

    void removeFromQueue(Long memberId, String gameMode);

    boolean acceptMatch(String matchId, Long memberId);

    boolean denyMatch(String matchId, Long memberId);
}