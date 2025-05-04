package com.tools.seoultech.timoproject.post.facade;

import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.domain.dto.Comment_SearchingFilterDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentFacade {
    CommentDTO.Response create(CommentDTO.Request requestDto);

    CommentDTO.Response read(Long id);
    List<CommentDTO.Response> searchPostByFilter(Comment_SearchingFilterDTO filterDto, Pageable pageable);

    CommentDTO.Response update(Long id, CommentDTO.Request requestDto);
    void delete(Long id);



}
