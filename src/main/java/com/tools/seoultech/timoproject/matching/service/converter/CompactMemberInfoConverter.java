package com.tools.seoultech.timoproject.matching.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Converter(autoApply = false)
public class CompactMemberInfoConverter implements AttributeConverter<CompactMemberInfo, String> {

    private final ObjectMapper objectMapper;

    public CompactMemberInfoConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(CompactMemberInfo attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public CompactMemberInfo convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : objectMapper.readValue(dbData, CompactMemberInfo.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
