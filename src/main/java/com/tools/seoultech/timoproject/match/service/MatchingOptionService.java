package com.tools.seoultech.timoproject.match.service;

import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionResponse;

public interface MatchingOptionService {

    MatchingOptionResponse updateMatchingOption(Long memberId, MatchingOptionRequest request);
    MatchingOptionResponse getMatchingOption(Long memberId);
}
