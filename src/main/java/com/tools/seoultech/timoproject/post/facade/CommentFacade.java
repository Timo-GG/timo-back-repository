package com.tools.seoultech.timoproject.post.facade;

import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;

import java.util.List;

public interface CommentFacade {
    CommentDTO.Response create(CommentDTO.Request requestDto);

    CommentDTO.Response read(Long id);
    List<CommentDTO.Response> readAll();
    List<CommentDTO.Response> readComments();

    CommentDTO.Response update(Long id, CommentDTO.Request requestDto);
    void delete(Long id);



}
