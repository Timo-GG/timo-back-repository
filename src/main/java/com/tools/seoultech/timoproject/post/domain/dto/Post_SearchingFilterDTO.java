package com.tools.seoultech.timoproject.post.domain.dto;

import com.tools.seoultech.timoproject.post.controller.validation.SearchingFilterCheck;
import com.tools.seoultech.timoproject.post.domain.entity.Category;
import lombok.Builder;

import java.util.Objects;

@SearchingFilterCheck
@Builder
public record Post_SearchingFilterDTO(
        Long postId,
        Long memberId,
        Category category,
        String sortBy,
        Boolean sortOrder
) {

    public Post_SearchingFilterDTO {
        if(Objects.isNull(sortBy))
            sortBy = "regDate";
        if(Objects.isNull(sortOrder))
            sortOrder = true;
    }
    static Post_SearchingFilterDTO of(
            Long postId, Long memberId, Category category,
            String sortBy, Boolean sortOrder
    ) {
        return new Post_SearchingFilterDTO(postId, memberId, category, sortBy, sortOrder);
    }
}
