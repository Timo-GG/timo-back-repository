package com.tools.seoultech.timoproject.post.service;

import com.tools.seoultech.timoproject.post.domain.dto.PageDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Post;

public interface PostService {
    PageDTO.Response<PostDTO.Response, Post> getList(PageDTO.Request request);
    PostDTO.Response read(Long id);

    PostDTO.Response entityToDto(Post entity);
    Post dtoToEntity(Object dto);
}
