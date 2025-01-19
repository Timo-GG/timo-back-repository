package com.tools.seoultech.timoproject.admin.service;

import com.tools.seoultech.timoproject.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcommentService {
    private final CommentRepository commentRepository;

    public void delete(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("해당 댓글을 찾을 수 없습니다. ID: " + id);
        }
    }
}
