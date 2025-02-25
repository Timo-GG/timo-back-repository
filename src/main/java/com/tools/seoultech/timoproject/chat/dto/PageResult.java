package com.tools.seoultech.timoproject.chat.dto;

import java.util.List;

public record PageResult<T>(
        List<T> values,
        Boolean hasNext) {

    public static <T> PageResult<T> of(List<T> values, Boolean hasNext) {
        return new PageResult<>(values, hasNext);
    }
}

