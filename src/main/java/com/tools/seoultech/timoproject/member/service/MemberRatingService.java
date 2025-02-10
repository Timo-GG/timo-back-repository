package com.tools.seoultech.timoproject.member.service;

import com.tools.seoultech.timoproject.rating.RatingResponse;

import java.util.List;

public interface MemberRatingService {

        List<RatingResponse> getMemberRatings(Long memberId);
}
