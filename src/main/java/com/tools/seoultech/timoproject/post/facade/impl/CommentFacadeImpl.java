package com.tools.seoultech.timoproject.post.facade.impl;

import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.domain.dto.Comment_SearchingFilterDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import com.tools.seoultech.timoproject.post.facade.CommentFacade;
import com.tools.seoultech.timoproject.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentFacadeImpl implements CommentFacade {
    private final CommentService commentService;

    @Override
    public CommentDTO.Response create(CommentDTO.Request requestDto) {
        Comment comment = commentService.create(requestDto);
        return commentService.entityToDto(comment);
    }

    @Override
    public CommentDTO.Response read(Long id) {
        Comment comment = commentService.read(id);
        return commentService.entityToDto(comment);
    }

    @Override
    public List<CommentDTO.Response> searchPostByFilter(Comment_SearchingFilterDTO filterDto, Pageable pageable) {
        List<Comment> commentList = commentService.searchCommentByFilter(filterDto, pageable);
        return commentList.stream()
                .map(commentService::entityToDto)
                .toList();
    }

    @Override
    public CommentDTO.Response update(Long id, CommentDTO.Request requestDto) {
        Comment comment = commentService.update(id, requestDto);
        return commentService.entityToDto(comment);
    }

    @Override
    public void delete(Long id) {
        commentService.delete(id);
    }
}
