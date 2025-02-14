package com.tools.seoultech.timoproject.admin.service.impl;

import com.tools.seoultech.timoproject.admin.AdminLog;
import com.tools.seoultech.timoproject.admin.AdminLogRepository;
import com.tools.seoultech.timoproject.admin.service.ApostService;
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
public class ApostServiceImpl implements ApostService {

    private final PostRepository postRepository;
    private final AdminLogRepository adminLogRepository;

    @Override
    public Page<Post> getList(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10); // 페이지 번호는 0부터 시작
        return postRepository.findAll(pageable);
    }

    @Override
    public Post get(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + id));
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void delete(Long id) {
        if (postRepository.existsById(id)) {
            // TODO: 어드민 아이디 커스텀화 필요
            AdminLog log = new AdminLog(AdminLog.EntityType.POST, AdminLog.MethodType.DELETE, id.toString(), "admin");
            adminLogRepository.save(log);
            postRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + id);
        }
    }
}
