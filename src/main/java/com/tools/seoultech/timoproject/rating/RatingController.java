package com.tools.seoultech.timoproject.rating;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.rating.dto.RatingRequest;
import com.tools.seoultech.timoproject.rating.dto.RatingResponse;
import com.tools.seoultech.timoproject.rating.dto.RatingTotalResponse;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("")
    public ResponseEntity<APIDataResponse<RatingResponse>> saveRating(@CurrentMemberId Long memberId, @RequestBody RatingRequest rating) {
        RatingResponse ratingResponse = ratingService.saveRating(memberId, rating);
        return ResponseEntity.ok(APIDataResponse.of(ratingResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping("")
    public ResponseEntity<APIDataResponse<RatingTotalResponse>> getRatings(@CurrentMemberId Long memberId) {
        RatingTotalResponse rating = ratingService.getRatings(memberId);
        return ResponseEntity.ok(APIDataResponse.of(rating));
    }

}
