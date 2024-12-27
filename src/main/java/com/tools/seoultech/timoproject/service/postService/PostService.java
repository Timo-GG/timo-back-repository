package com.tools.seoultech.timoproject.service.postService;

import com.tools.seoultech.timoproject.domain.Post;
import com.tools.seoultech.timoproject.dto.PageDTO;
import com.tools.seoultech.timoproject.dto.PostDTO;

public interface PostService {
    PageDTO.Response<PostDTO, Post> getList(PageDTO.Request request);
    PostDTO read(Long id);

    default PostDTO entityToDto(Post entity){
        // ObjectMapper 사용하면 되는 부분 아닌가?
        PostDTO dto = PostDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }
    default Post dtoToEntity(PostDTO dto){
        Post entity = Post.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }
}
