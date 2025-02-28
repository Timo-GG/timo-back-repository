package com.tools.seoultech.timoproject.post.facade.impl;

import com.tools.seoultech.timoproject.post.domain.dto.PageDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.dto.SearchingFilterDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Category;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.facade.PostFacade;
import com.tools.seoultech.timoproject.post.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostFacadeImpl implements PostFacade {
    private final PostServiceImpl postService;

    @Override
    public PageDTO.Response<PostDTO.Response, Post> getList(PageDTO.Request request) {
        return postService.getList(request);
    }

    @Override
    public PostDTO.Response create(PostDTO.Request requestDto) {
        Post post = postService.create(requestDto);
        return postService.entityToDto(post);
    }

    @Override
    public PostDTO.Response read(Long postId) {
        Post post = postService.read(postId);
        return postService.entityToDto(post);
    }

    @Override
    public List<PostDTO.Response> searchByFilter(
            SearchingFilterDTO filterDto,
            Pageable pageable
    ) {
        List<Post> postList = postService.searchByFilter(filterDto, pageable);
        return postList
                .stream()
                .map(postService::entityToDto)
                .toList();
    }

    @Override
    public PostDTO.Response update(Long postId, PostDTO.Request request) {
        Post post = postService.update(postId, request);
        return postService.entityToDto(post);
    }

    @Override
    public void delete(Long postId) {
        postService.delete(postId);
    }

    @Override
    public PostDTO.Response increaseLikeCount(Long postId, Long memberId) {
        Post post = postService.increaseLikeCount(postId, memberId);
        return postService.entityToDto(post);
    }

    @Override
    public PostDTO.Response decreaseLikeCount(Long postId, Long memberId) {
        Post post = postService.decreaseLikeCount(postId, memberId);
        return postService.entityToDto(post);
    }
}
