package com.tools.seoultech.timoproject.post.service;

<<<<<<< HEAD
import com.tools.seoultech.timoproject.post.domain.Post;
import com.tools.seoultech.timoproject.post.dto.PageDTO;
import com.tools.seoultech.timoproject.post.dto.PostDTO;
=======
import com.tools.seoultech.timoproject.post.domain.dto.PostDtoRequest;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.domain.dto.PageDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;

>>>>>>> #12-crud-repository

public interface PostService {
    PageDTO.Response<PostDTO, Post> getList(PageDTO.Request request);
    PostDTO read(Long id);

<<<<<<< HEAD
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
=======
    PostDTO entityToDto(Post entity);
    Post dtoToEntity(PostDTO dto);
>>>>>>> #12-crud-repository
}
