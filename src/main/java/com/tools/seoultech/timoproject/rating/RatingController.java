package com.tools.seoultech.timoproject.rating;

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
    public ResponseEntity<APIDataResponse<RatingResponse>> saveRating(@RequestBody RatingRequest rating) {
        RatingResponse ratingResponse = ratingService.saveRating(rating);
        return ResponseEntity.ok(APIDataResponse.of(ratingResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping("")
    public ResponseEntity<APIDataResponse<RatingTotalResponse>> getRatings() {
        RatingTotalResponse rating = ratingService.getRatings(1L);
        return ResponseEntity.ok(APIDataResponse.of(rating));
    }

}
