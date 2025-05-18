package com.tools.seoultech.timoproject.matching.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = false)
public class UserInfoConverter implements AttributeConverter<UserInfo, String> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(UserInfo attribute) {
        if (attribute == null) return null;
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON write error", e);
        }
    }

    @Override
    public UserInfo convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return mapper.readValue(dbData, UserInfo.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("JSON read error", e);
        }
    }
}
