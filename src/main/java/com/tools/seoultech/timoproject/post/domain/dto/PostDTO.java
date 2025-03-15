package com.tools.seoultech.timoproject.post.domain.dto;

import com.tools.seoultech.timoproject.post.domain.entity.Category;
import com.tools.seoultech.timoproject.post.domain.entity.Image;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
            String memberName,  // Todo: NEW
            Category category,

            Integer viewCount,
            Integer likeCount,
            Integer commentCount,  // Todo: New
            Integer imageCount,  // Todo: New

            LocalDateTime regDate,
            LocalDateTime modDate
    ) {

    }
}
