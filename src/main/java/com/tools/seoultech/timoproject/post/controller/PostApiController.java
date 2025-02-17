package com.tools.seoultech.timoproject.post.controller;

import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDtoRequest;
import com.tools.seoultech.timoproject.post.service.PostServiceImpl;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postApi")
@RequiredArgsConstructor
public class PostApiController {
    private final PostServiceImpl postService;

    @GetMapping("/read/{postId}")
    public ResponseEntity<APIDataResponse<PostDTO>> readPost(@PathVariable("postId") Long postId) {
        PostDTO postDto = postService.read(postId);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(postDto));
    }
    @GetMapping("/read/getAll")
    public ResponseEntity<APIDataResponse<List<PostDTO>>> getAllPosts() {
        List<PostDTO> postDtoList = postService.readAll();
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(postDtoList));
    }
    @PostMapping("/create")
    public ResponseEntity<APIDataResponse<PostDTO>> createPost(@RequestBody PostDtoRequest postDto) {
        System.err.println(postDto.toString());
        PostDTO dto = postService.create(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(APIDataResponse.of(dto));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<APIDataResponse<PostDTO>> updatePost(
            @PathVariable Long id, @RequestBody PostDtoRequest postDto
    ) {
        PostDTO dto = postService.update(id, postDto);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(dto));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIDataResponse> deletePost(@PathVariable("id") Long id) {
        postService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.empty());
    }
    @GetMapping("/likeCount/increase/{id}")
    public ResponseEntity<APIDataResponse> increaseLikeCount(@PathVariable("id") Long id) {
        PostDTO responseDto = postService.increaseLikeCount(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(responseDto));
    }
    @GetMapping("/likeCount/decrease/{id}")
    public ResponseEntity<APIDataResponse> decreaseLikeCount(@PathVariable("id") Long id) {
        PostDTO responseDto = postService.decreaseLikeCount(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(responseDto));
    }
}
