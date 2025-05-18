package com.tools.seoultech.timoproject.matching.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class ListCompactMemberInfoConverter implements AttributeConverter<List<CompactMemberInfo>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<CompactMemberInfo> attribute) {
        if (attribute == null) return null;
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("List<CompactMemberInfo> -> JSON 변환 실패", e);
        }
    }

    @Override
    public List<CompactMemberInfo> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<CompactMemberInfo>>() {});
        } catch (IOException e) {
            throw new RuntimeException("JSON -> List<CompactMemberInfo> 변환 실패", e);
        }
    }
}
