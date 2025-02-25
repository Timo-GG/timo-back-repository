package com.tools.seoultech.timoproject.post.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostDtoRequest {
    private String title;
    private String content;
    private Long memberId;
}
