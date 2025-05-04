package com.tools.seoultech.timoproject.post.service;

import com.tools.seoultech.timoproject.post.domain.dto.PageDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.dto.Post_SearchingFilterDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    PostDTO.Response entityToDto(Post entity);
    Post dtoToEntity(Object dto);

    PageDTO.Response<PostDTO.Response, Post> getList(PageDTO.Request request);

    // Post CRUD
    Post create(PostDTO.Request postDto);
    Post read(Long id);
    Post update(Long id, PostDTO.Request request);
    void delete(Long id);

    // PostLike
    Post increaseLikeCount(Long postId, Long memberId);
    Post decreaseLikeCount(Long postId, Long memberId);

    // Read by Filtering
    List<Post> searchPostByFilter(Post_SearchingFilterDTO filterDto, Pageable pageable);
}
