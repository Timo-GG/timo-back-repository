package com.tools.seoultech.timoproject.post.service;

import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment dtoToEntity(Object dto);
    CommentDTO.Response entityToDto(Comment comment);

    CommentDTO.Response create(CommentDTO.Request dto);
    CommentDTO.Response read(Long id);
    List<CommentDTO.Response> readAll();
    CommentDTO.Response update(Long id, CommentDTO.Request requestDto);
    void delete(Long id);
}
