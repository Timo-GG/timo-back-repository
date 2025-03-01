package com.tools.seoultech.timoproject.post.service;

import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment dtoToEntity(Object dto);
    CommentDTO.Response entityToDto(Comment comment);

    Comment create(CommentDTO.Request dto);
    Comment read(Long id);
    List<Comment> readAll();
    Comment update(Long id, CommentDTO.Request requestDto);
    void delete(Long id);
}
