package com.tools.seoultech.timoproject.rating.dto;

import com.tools.seoultech.timoproject.rating.Rating;

import java.math.BigDecimal;

public record RatingRequest(BigDecimal score, Rating.Attitude attitude, Rating.Speech speech, Rating.Skill skill) {
}

