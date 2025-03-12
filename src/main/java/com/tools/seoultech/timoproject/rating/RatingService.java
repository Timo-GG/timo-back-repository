package com.tools.seoultech.timoproject.rating;

import com.tools.seoultech.timoproject.rating.dto.RatingRequest;
import com.tools.seoultech.timoproject.rating.dto.RatingResponse;
import com.tools.seoultech.timoproject.rating.dto.RatingTotalResponse;

import java.math.BigDecimal;
import java.util.List;

public interface RatingService {

        RatingResponse saveRating(Long memberId, RatingRequest rating);

        void deleteRating(Long id);

        RatingTotalResponse getRatings(Long memberId);

        BigDecimal getRatingAverage(Long memberId);

}
