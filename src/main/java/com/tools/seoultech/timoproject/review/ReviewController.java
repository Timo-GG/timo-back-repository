package com.tools.seoultech.timoproject.review;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
@Tag(name = "Review", description = "리뷰 API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.")
    public ResponseEntity<ReviewResponseDto> createReview(
            @CurrentMemberId Long currentMemberId,
            @RequestBody ReviewRequestDto dto) {
        ReviewResponseDto response = reviewService.createReview(currentMemberId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "내가 받은 리뷰 조회", description = "내가 받은 리뷰 목록을 조회합니다.")
    public ResponseEntity<List<ReviewResponseDto>> getMyReceivedReviews(
            @CurrentMemberId Long currentMemberId) {
        List<ReviewResponseDto> responses = reviewService.getReviewsByReviewee(currentMemberId);
        return ResponseEntity.ok(responses);
    }
}