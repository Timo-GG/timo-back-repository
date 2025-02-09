package com.tools.seoultech.timoproject.rating;

import jakarta.persistence.EntityNotFoundException;

public class RatingServiceImpl implements RatingService{

        private final RatingRepository ratingRepository;

        public RatingServiceImpl(RatingRepository ratingRepository) {
            this.ratingRepository = ratingRepository;
        }

        @Override
        public void saveRating(RatingReq ratingReq) {

            Rating rating = Rating.builder()
                    .score(ratingReq.score())
                    .attitude(ratingReq.attitude())
                    .speech(ratingReq.speech())
                    .skill(ratingReq.skill())
                    .build();

            ratingRepository.save(rating);
        }

        @Override
        public void deleteRating(Long id) {
            ratingRepository.deleteById(id);
        }

    @Override
    public RatingRes getRating(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found with id: " + id));
        return new RatingRes(rating.getId(), rating.getScore(), rating.getAttitude(), rating.getSpeech(), rating.getSkill());
    }
}
