<<<<<<<< HEAD:src/main/java/com/tools/seoultech/timoproject/post/dto/PostDTO.java
package com.tools.seoultech.timoproject.post.dto;
========
package com.tools.seoultech.timoproject.post.domain.dto;
>>>>>>>> #12-crud-repository:src/main/java/com/tools/seoultech/timoproject/post/domain/dto/PostDTO.java

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
    private String puuid;
    private LocalDateTime regDate, modDate;
}

