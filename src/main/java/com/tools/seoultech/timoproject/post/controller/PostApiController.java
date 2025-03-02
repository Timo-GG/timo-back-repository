package com.tools.seoultech.timoproject.post.controller;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.dto.SearchingFilterDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Category;
import com.tools.seoultech.timoproject.post.facade.PostFacade;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostApiController {
    private final PostFacade postFacade;

    @GetMapping("/public/{postId}")
    public ResponseEntity<APIDataResponse<PostDTO.Response>> readPost(
            @PathVariable("postId") Long postId
    ) {
        PostDTO.Response postDto = postFacade.read(postId);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(postDto));
    }
    @GetMapping("/public")
    public ResponseEntity<APIDataResponse<List<PostDTO.Response>>> readPosts(
            @Valid @ModelAttribute SearchingFilterDTO filterDto,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        List<PostDTO.Response> postList = postFacade
                .searchByFilter(
                        filterDto,
                        pageable
                );
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(postList));
    }
    @PostMapping("")
    public ResponseEntity<APIDataResponse<PostDTO.Response>> createPost(@RequestBody PostDTO.Request postDto) {
        System.err.println(postDto.toString());
        PostDTO.Response dto = postFacade.create(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(APIDataResponse.of(dto));
    }
    @PutMapping("/{postId}")
    public ResponseEntity<APIDataResponse<PostDTO.Response>> updatePost(
            @PathVariable Long postId, @RequestBody PostDTO.Request postDto
    ) {
        PostDTO.Response dto = postFacade.update(postId, postDto);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(dto));
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<APIDataResponse> deletePost(@PathVariable("postId") Long postId) {
        postFacade.delete(postId);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.empty());
    }

    @GetMapping("/{postId}/likeCount/increase")
    public ResponseEntity<APIDataResponse> increaseLikeCount(
            @PathVariable("postId") Long postId, @CurrentMemberId Long memberId
    ) {
        PostDTO.Response responseDto = postFacade.increaseLikeCount(postId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(responseDto));
    }
    @GetMapping("/{postId}/likeCount/decrease")
    public ResponseEntity<APIDataResponse> decreaseLikeCount(
            @PathVariable("postId") Long postId, @CurrentMemberId Long memberId
    ) {
        PostDTO.Response responseDto = postFacade.decreaseLikeCount(postId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(APIDataResponse.of(responseDto));
    }
}
