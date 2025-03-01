package com.tools.seoultech.timoproject.post.facade.impl;

import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import com.tools.seoultech.timoproject.post.facade.CommentFacade;
import com.tools.seoultech.timoproject.post.service.CommentService;
import lombok.RequiredArgsConstructor;
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
    public List<CommentDTO.Response> readAll() {
        List<Comment> commentList = commentService.readAll();
        return commentList.stream()
                .map(commentService::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO.Response> readComments() {
        // TODO: 필터링 검색 메서드
        return null;
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
