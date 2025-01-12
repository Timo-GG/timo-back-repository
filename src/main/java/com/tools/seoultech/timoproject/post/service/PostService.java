package com.tools.seoultech.timoproject.post.service;

import com.tools.seoultech.timoproject.post.domain.dto.PostDtoRequest;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.domain.dto.PageDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;


public interface PostService {
    PageDTO.Response<PostDTO, Post> getList(PageDTO.Request request);
    PostDTO read(Long id);

    PostDTO entityToDto(Post entity);
    Post dtoToEntity(PostDTO dto);
}
