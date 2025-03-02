package com.tools.seoultech.timoproject.post.domain.dto;

import com.tools.seoultech.timoproject.post.controller.validation.SearchingFilterCheck;
import com.tools.seoultech.timoproject.post.domain.entity.Category;
import lombok.Builder;

import java.util.Objects;

@SearchingFilterCheck
@Builder
public record SearchingFilterDTO(
        Long memberId,
        Category category,
        String sortBy,
        Boolean sortOrder
) {

    public SearchingFilterDTO{
        if(Objects.isNull(sortBy))
            sortBy = "regDate";
        if(Objects.isNull(sortOrder))
            sortOrder = true;
    }
    static SearchingFilterDTO of(
            Long memberId, Category category,
            String sortBy, Boolean sortOrder
    ) {
        return new SearchingFilterDTO(memberId, category, sortBy, sortOrder);
    }
}
