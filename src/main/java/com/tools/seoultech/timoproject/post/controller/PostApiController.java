package com.tools.seoultech.timoproject.post.controller;

import com.tools.seoultech.timoproject.post.dto.PostDTO;
import com.tools.seoultech.timoproject.post.service.PostService;
import com.tools.seoultech.timoproject.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postApi")
public class PostApiController {
    private final PostService postService;

    @GetMapping("/read/{postId}")
    public PostDTO readPost(@PathVariable("postId") Long postId) {
        return postService.read(postId);
    }
}
