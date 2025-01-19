package com.tools.seoultech.timoproject.admin.service;

import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApostService {
    private final PostRepository postRepository;

    // 게시글 목록 조회 (페이지네이션)
    public Page<Post> getList(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10); // 페이지 번호는 0부터 시작
        return postRepository.findAll(pageable);
    }

    // 특정 게시글 조회
    public Post get(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + id));
    }

    // 게시글 저장
    public Post save(Post post) {
        return postRepository.save(post);
    }

    // 게시글 삭제
    public void delete(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + id);
        }
    }
}
