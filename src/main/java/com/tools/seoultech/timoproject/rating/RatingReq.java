package com.tools.seoultech.timoproject.rating;

import java.math.BigDecimal;

public record RatingReq(BigDecimal score, Rating.Attitude attitude, Rating.Speech speech, Rating.Skill skill) {
}

