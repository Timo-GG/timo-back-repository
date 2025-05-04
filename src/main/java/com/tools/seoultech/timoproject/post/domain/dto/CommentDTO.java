package com.tools.seoultech.timoproject.post.domain.dto;

import lombok.Builder;
import java.time.LocalDateTime;

public class CommentDTO {
    @Builder
    public record Request(
            String content,
            Long memberId,
            Long postId
    ){

    }
    @Builder
    public record Response(
            Long id,
            String content,
            Long postId,
            Long memberId,
            LocalDateTime regDate,
            LocalDateTime modDate
    ){

    }
}
