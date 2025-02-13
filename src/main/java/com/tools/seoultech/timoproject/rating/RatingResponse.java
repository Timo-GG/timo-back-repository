package com.tools.seoultech.timoproject.rating;

import java.math.BigDecimal;

public record RatingResponse(Long id, Long memberId, Long duoId, BigDecimal score, Rating.Attitude attitude, Rating.Speech speech, Rating.Skill skill) {
}
