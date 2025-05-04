package com.tools.seoultech.timoproject.post.controller;

import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.domain.dto.Comment_SearchingFilterDTO;
import com.tools.seoultech.timoproject.post.facade.CommentFacade;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Post API", description = "Post API 테스트")
public class CommentApiController {
    private final CommentFacade commentFacade;

    @GetMapping("/public/{commentId}")
    @Tag(name = "Post API")
    @Operation(summary = "단일 Comment 조회", description = "스웨거 테스트용 API")
    public ResponseEntity<APIDataResponse<CommentDTO.Response>> readComment (
            @PathVariable Long commentId
    ) {
        CommentDTO.Response readDto = commentFacade.read(commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIDataResponse.of(readDto));
    }
    @GetMapping("/public")
    public ResponseEntity<APIDataResponse<List<CommentDTO.Response>>> readComments(
            @Valid @ModelAttribute Comment_SearchingFilterDTO filterDto,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        List<CommentDTO.Response> readDtoList = commentFacade.searchPostByFilter(filterDto, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIDataResponse.of(readDtoList));
    }
    @PostMapping
    public ResponseEntity<APIDataResponse<CommentDTO.Response>> createComment(
            @RequestBody CommentDTO.Request commentDTO
    ) {
        CommentDTO.Response responseDto = commentFacade.create(commentDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIDataResponse.of(responseDto));
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<APIDataResponse<CommentDTO.Response>> updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDTO.Request commentDTO
    ){
        CommentDTO.Response responseDto = commentFacade.update(commentId, commentDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIDataResponse.of(responseDto));
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<APIDataResponse<CommentDTO.Response>> deleteComment(
            @PathVariable("commentId") Long commentId
    ){
        commentFacade.delete(commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIDataResponse.empty());
    }
}
