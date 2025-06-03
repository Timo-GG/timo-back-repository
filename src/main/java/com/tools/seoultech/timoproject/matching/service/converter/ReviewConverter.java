package com.tools.seoultech.timoproject.matching.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.review.Review;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

public class ReviewConverter implements AttributeConverter<Review, String> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Review review) {
        if (review == null) return null;
        try {
            return mapper.writeValueAsString(review);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Review JSON 직렬화 실패", e);
        }
    }

    @Override
    public Review convertToEntityAttribute(String json) {
        if (json == null || json.isBlank()) return new Review(); // 💡 NPE 방지
        try {
            return mapper.readValue(json, Review.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Review JSON 역직렬화 실패", e);
        }
    }
}
