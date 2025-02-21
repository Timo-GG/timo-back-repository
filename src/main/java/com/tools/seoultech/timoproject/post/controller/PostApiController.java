package com.tools.seoultech.timoproject.post.controller;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.service.PostServiceImpl;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostApiController {
    private final PostServiceImpl postService;

    @GetMapping("/public/{postId}")
    public ResponseEntity<APIDataResponse<PostDTO.Response>> readPost(@PathVariable("postId") Long postId) {
        PostDTO.Response postDto = postService.read(postId);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(postDto));
    }

    @GetMapping("/member")
    public ResponseEntity<APIDataResponse<List<PostDTO.Response>>> readPostsByMember(
            @CurrentMemberId Long memberId
    ) {
        List<PostDTO.Response> postList =  postService.readByMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(postList));
    }
    @GetMapping("/public")
    public ResponseEntity<APIDataResponse<List<PostDTO.Response>>> readAllPosts() {
        List<PostDTO.Response> postDtoList = postService.readAll();
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(postDtoList));
    }
    @PostMapping("")
    public ResponseEntity<APIDataResponse<PostDTO.Response>> createPost(@RequestBody PostDTO.Request postDto) {
        System.err.println(postDto.toString());
        PostDTO.Response dto = postService.create(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(APIDataResponse.of(dto));
    }
    @PutMapping("/{postId}")
    public ResponseEntity<APIDataResponse<PostDTO.Response>> updatePost(
            @PathVariable Long postId, @RequestBody PostDTO.Request postDto
    ) {
        PostDTO.Response dto = postService.update(postId, postDto);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(dto));
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<APIDataResponse> deletePost(@PathVariable("postId") Long postId) {
        postService.delete(postId);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.empty());
    }
    @GetMapping("/{postId}/likeCount/increase")
    public ResponseEntity<APIDataResponse> increaseLikeCount(
            @PathVariable("postId") Long postId, @CurrentMemberId Long memberId
    ) {
        PostDTO.Response responseDto = postService.increaseLikeCount(postId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(responseDto));
    }
    @GetMapping("/{postId}/likeCount/decrease")
    public ResponseEntity<APIDataResponse> decreaseLikeCount(
            @PathVariable("postId") Long postId, @CurrentMemberId Long memberId
    ) {
        PostDTO.Response responseDto = postService.decreaseLikeCount(postId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(responseDto));
    }
}
