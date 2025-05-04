package com.tools.seoultech.timoproject.rating.dto;

import java.math.BigDecimal;
import java.util.List;

public record RatingTotalResponse(BigDecimal average, List<RatingResponse> ratings) {
}
