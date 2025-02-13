package com.tools.seoultech.timoproject.match.service;

import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionResponse;
import com.tools.seoultech.timoproject.member.domain.Member;

import java.util.Optional;

public interface MatchingService {

    void addToMatchingQueue(Long memberId, MatchingOptionRequest request);

    Optional<Member> findMatch(Long memberId);

    void removeFromQueue(Long memberId, String gameMode);
}