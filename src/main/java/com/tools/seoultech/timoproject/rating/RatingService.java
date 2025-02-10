package com.tools.seoultech.timoproject.rating;

public interface RatingService {

        void saveRating(RatingRequest rating);

        void deleteRating(Long id);

        RatingResponse getRating(Long id);

}
