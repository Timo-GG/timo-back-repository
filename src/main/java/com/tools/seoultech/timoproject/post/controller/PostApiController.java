package com.tools.seoultech.timoproject.post.controller;

import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.service.PostServiceImpl;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/postApi")
@RequiredArgsConstructor
public class PostApiController {
    private final PostServiceImpl postService;

    @GetMapping("/read/{postId}")
    public ResponseEntity<APIDataResponse<PostDTO.Response>> readPost(@PathVariable("postId") Long postId) {
        PostDTO.Response postDto = postService.read(postId);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(postDto));
    }
    @GetMapping("/read/getAll")
    public ResponseEntity<APIDataResponse<List<PostDTO.Response>>> getAllPosts() {
        List<PostDTO.Response> postDtoList = postService.readAll();
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(postDtoList));
    }
    @PostMapping("/create")
    public ResponseEntity<APIDataResponse<PostDTO.Response>> createPost(@RequestBody PostDTO.Request postDto) {
        System.err.println(postDto.toString());
        PostDTO.Response dto = postService.create(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(APIDataResponse.of(dto));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<APIDataResponse<PostDTO.Response>> updatePost(
            @PathVariable Long id, @RequestBody PostDTO.Request postDto
    ) {
        PostDTO.Response dto = postService.update(id, postDto);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(dto));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIDataResponse> deletePost(@PathVariable("id") Long id) {
        postService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.empty());
    }
    @GetMapping("/likeCount/increase/{id}")
    public ResponseEntity<APIDataResponse> increaseLikeCount(@PathVariable("id") Long id) {
        PostDTO.Response responseDto = postService.increaseLikeCount(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(responseDto));
    }
    @GetMapping("/likeCount/decrease/{id}")
    public ResponseEntity<APIDataResponse> decreaseLikeCount(@PathVariable("id") Long id) {
        PostDTO.Response responseDto = postService.decreaseLikeCount(id);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(responseDto));
    }
}
