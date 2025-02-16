package com.tools.seoultech.timoproject.post.domain.dto;

import com.tools.seoultech.timoproject.post.domain.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
public record PostDTO (
        Long id,
        String title,
        String content,
        Long memberId,
        Category category,
        LocalDateTime regDate,
        LocalDateTime modDate
){
    public PostDTO of(String title, String content, Long memberId, Category category){
        return PostDTO.builder()
                .title(title)
                .content(content)
                .memberId(memberId)
                .category(category)
                .build();
    }
}

