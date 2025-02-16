package com.tools.seoultech.timoproject.post.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record PostDtoRequest (
    String title,
    String content,
    Long memberId
){

}
