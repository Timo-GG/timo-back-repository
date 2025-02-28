package com.tools.seoultech.timoproject.post.facade;

import com.tools.seoultech.timoproject.post.domain.dto.PageDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.dto.SearchingFilterDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Category;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostFacade {
    PageDTO.Response<PostDTO.Response, Post> getList(PageDTO.Request request);

    PostDTO.Response create(PostDTO.Request request);
    PostDTO.Response read(Long postId);
    PostDTO.Response update(Long postId, PostDTO.Request requestDto);
    void delete(Long postId);

    PostDTO.Response increaseLikeCount(Long postId, Long memberId);
    PostDTO.Response decreaseLikeCount(Long postId, Long memberId);

    List<PostDTO.Response> searchByFilter(
            SearchingFilterDTO filterDto,
            Pageable pageable

    );
    // Post별 Comment > Comment
}
