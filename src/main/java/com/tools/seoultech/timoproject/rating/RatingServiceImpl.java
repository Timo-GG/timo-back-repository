package com.tools.seoultech.timoproject.rating;

import jakarta.persistence.EntityNotFoundException;

public class RatingServiceImpl implements RatingService{

        private final RatingRepository ratingRepository;

        public RatingServiceImpl(RatingRepository ratingRepository) {
            this.ratingRepository = ratingRepository;
        }

        @Override
        public void saveRating(RatingRequest RatingRequest) {

            Rating rating = Rating.builder()
                    .score(RatingRequest.score())
                    .attitude(RatingRequest.attitude())
                    .speech(RatingRequest.speech())
                    .skill(RatingRequest.skill())
                    .build();

            ratingRepository.save(rating);
        }

        @Override
        public void deleteRating(Long id) {
            ratingRepository.deleteById(id);
        }

    @Override
    public RatingResponse getRating(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found with id: " + id));
        return new RatingResponse(rating.getId(), rating.getMember().getId(), rating.getDuo().getId(), rating.getScore(), rating.getAttitude(), rating.getSpeech(), rating.getSkill());
    }
}
