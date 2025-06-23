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

    @PostMapping("/{myPageId}")
    @Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.")
    public ResponseEntity<Review> createReview(
            @CurrentMemberId Long currentMemberId,
            @PathVariable Long myPageId,
            @RequestBody ReviewRequestDto dto) {
        Review response = reviewService.createReview(myPageId, currentMemberId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{myPageId}")
    @Operation(summary = "내 마이페이지 리뷰 조회", description = "해당 마이페이지의 내 리뷰를 조회합니다.")
    public ResponseEntity<Review> getMyReceivedReviews(
            @CurrentMemberId Long currentMemberId,
            @PathVariable Long myPageId) {
        Review response = reviewService.getReviews(myPageId, currentMemberId);
        return ResponseEntity.ok(response);
    }
}