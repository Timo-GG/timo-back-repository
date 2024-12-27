package com.tools.seoultech.timoproject.service.postService;

import com.tools.seoultech.timoproject.domain.Post;
import com.tools.seoultech.timoproject.dto.PageDTO;
import com.tools.seoultech.timoproject.dto.PostDTO;
import com.tools.seoultech.timoproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public PageDTO.Response<PostDTO, Post> getList(PageDTO.Request request){
        Pageable pageable = request.getPageable(Sort.by("id").descending());
        Page<Post> result = postRepository.findAll(pageable);
        Function<Post, PostDTO> fn = this::entityToDto;
        return PageDTO.Response.of(result, fn);
    }
    public PostDTO read(Long id){
        Optional<Post> post = postRepository.findById(id);
        return post.isPresent() ? entityToDto(post.get()) : null;
    }
}
