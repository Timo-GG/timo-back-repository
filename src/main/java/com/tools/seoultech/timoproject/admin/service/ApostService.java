package com.tools.seoultech.timoproject.admin.service;

import com.tools.seoultech.timoproject.post.domain.entity.Post;
import org.springframework.data.domain.Page;

public interface ApostService {
    // 게시글 목록 조회 (페이지네이션)
    Page<Post> getList(int pageNumber);

    // 특정 게시글 조회
    Post get(Long id);

    // 게시글 저장
    Post save(Post post);

    // 게시글 삭제
    void delete(Long id);
}
