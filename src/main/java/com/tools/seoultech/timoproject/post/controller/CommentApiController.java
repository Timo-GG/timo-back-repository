package com.tools.seoultech.timoproject.post.controller;

import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.facade.CommentFacade;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentApiController {
    private final CommentFacade commentFacade;

    @GetMapping("/public/{commentId}")
    public ResponseEntity<APIDataResponse<CommentDTO.Response>> readComment (
            @PathVariable Long commentId
    ) {
        CommentDTO.Response readDto = commentFacade.read(commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIDataResponse.of(readDto));
    }
    @GetMapping("/public")
    public ResponseEntity<APIDataResponse<List<CommentDTO.Response>>> readAllComments() {
        List<CommentDTO.Response> readDtoList = commentFacade.readAll();
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
