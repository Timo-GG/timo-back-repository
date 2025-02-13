package com.tools.seoultech.timoproject.post.domain.dto;

import com.tools.seoultech.timoproject.post.domain.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private Category category;
    private LocalDateTime regDate, modDate;
}

