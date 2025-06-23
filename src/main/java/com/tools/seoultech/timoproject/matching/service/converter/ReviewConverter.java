package com.tools.seoultech.timoproject.matching.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.review.Review;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

public class ReviewConverter implements AttributeConverter<Review, String> {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // JavaTimeModule 등록으로 LocalDateTime 지원
        mapper.registerModule(new JavaTimeModule());
        // 타임스탬프 대신 ISO 형식으로 직렬화
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

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
