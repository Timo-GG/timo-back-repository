package com.tools.seoultech.timoproject.rating;

import java.math.BigDecimal;

public record RatingRequest(BigDecimal score, Rating.Attitude attitude, Rating.Speech speech, Rating.Skill skill) {
}

