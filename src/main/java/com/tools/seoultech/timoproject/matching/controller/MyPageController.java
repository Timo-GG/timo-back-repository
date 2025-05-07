package com.tools.seoultech.timoproject.matching.controller;

import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisMyPage;
import com.tools.seoultech.timoproject.matching.service.MatchingService;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
@Tag(name = "MyPage", description = "Matching API")
public class MyPageController {
    private final MatchingService matchingService;

    @GetMapping("/myPage/{myPageUUID}")
    public ResponseEntity<APIDataResponse<MyPageDTO.ResponseMyPage>> getMyPage(@PathVariable UUID myPageUUID) throws Exception{
        MyPageDTO.ResponseMyPage testDto = matchingService.getMyPage(myPageUUID);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(testDto)
                );
    }
    @GetMapping("/myPage/category/{category}")
    public ResponseEntity<APIDataResponse<List<MyPageDTO.ResponseMyPage>>> getMyPage(@PathVariable MatchingCategory category) throws Exception{
        List<MyPageDTO.ResponseMyPage> testDtoList = matchingService.getMyPage(category);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(testDtoList)
                );
    }
    @PostMapping("/myPage")
    public ResponseEntity<APIDataResponse<RedisMyPage>> createMyPage(@RequestBody MatchingDTO.RequestDuo matchingDuo) throws Exception{
        System.err.println("MyPage Controller @Post");
        RedisMyPage testDto = matchingService.saveDuoMatchingToMyPage(matchingDuo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(testDto)
                );
    }
    @DeleteMapping("/myPage/{myPageUUID}")
    public ResponseEntity<APIDataResponse> deleteMyPage(@PathVariable UUID myPageUUID) throws Exception{
        matchingService.deleteMyPage(myPageUUID);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.empty()
                );
    }
    @DeleteMapping("/myPage")
    public ResponseEntity<APIDataResponse> deleteAllMyPage ()throws Exception{
        matchingService.deleteAllMyPage();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.empty()
                );
    }
}
