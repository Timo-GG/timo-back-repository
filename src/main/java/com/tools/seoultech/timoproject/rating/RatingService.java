package com.tools.seoultech.timoproject.rating;

public interface RatingService {

        void saveRating(RatingReq rating);

        void deleteRating(Long id);

        RatingRes getRating(Long id);

}
