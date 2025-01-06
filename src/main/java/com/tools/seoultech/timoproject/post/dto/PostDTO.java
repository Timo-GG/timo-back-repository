package com.tools.seoultech.timoproject.post.dto;

import com.tools.seoultech.timoproject.domain.UserAccount;
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
    private String puuid;
    private LocalDateTime regDate, modDate;
}

