package com.tools.seoultech.timoproject.post.domain.dto;

import com.tools.seoultech.timoproject.post.domain.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostDTO {
    @Builder
    public record Request (
            String title,
            String content,
            Category category,
            Long memberId
    ){

    }
    @Builder
    public record Response(
            Long id,
            String title,
            String content,
            Long memberId,
            Category category,
            Integer viewCount,
            Integer likeCount,
            LocalDateTime regDate,
            LocalDateTime modDate
    ) {

    }
}
