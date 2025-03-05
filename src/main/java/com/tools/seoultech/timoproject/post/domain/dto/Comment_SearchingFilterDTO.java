package com.tools.seoultech.timoproject.post.domain.dto;

import lombok.Builder;

import java.util.Objects;

@Builder
public record Comment_SearchingFilterDTO(
        Long commentId,
        Long postId,
        Long memberId,
        String sortBy,
        Boolean sortOrder
) {
    public Comment_SearchingFilterDTO {
        if(Objects.isNull(sortBy))
            sortBy = "regDate";
        if(Objects.isNull(sortOrder))
            sortOrder = true;
    }

    public static Comment_SearchingFilterDTO of (
            Long commentId,
            Long postId,
            Long memberId,
            String sortBy,
            Boolean sortOrder
    ){
        return new Comment_SearchingFilterDTO(commentId, postId, memberId, sortBy, sortOrder);
    }
}
