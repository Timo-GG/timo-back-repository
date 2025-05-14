package com.tools.seoultech.timoproject.matching.controller;

import com.tools.seoultech.timoproject.global.annotation.swagger.RedisOperation;
import com.tools.seoultech.timoproject.global.annotation.swagger.ResponseOperation;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisMyPage;
import com.tools.seoultech.timoproject.matching.service.MatchingService;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @ResponseOperation
    public ResponseEntity<APIDataResponse<MyPageDTO.ResponseMyPage>> getMyPage(@PathVariable UUID myPageUUID) throws Exception{
        MyPageDTO.ResponseMyPage testDto = matchingService.getMyPage(myPageUUID);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(testDto)
                );
    }

    @GetMapping("/myPage/category/{category}")
    @ResponseOperation
    public ResponseEntity<APIDataResponse<List<MyPageDTO.ResponseMyPage>>> getMyPage(@PathVariable MatchingCategory category) throws Exception{
        List<MyPageDTO.ResponseMyPage> testDtoList = matchingService.getMyPage(category);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(testDtoList)
                );
    }

    @PostMapping("/myPage/duo")
    @RedisOperation
    public ResponseEntity<APIDataResponse<RedisMyPage>> createDuoMyPage(@RequestBody MatchingDTO.RequestDuo matchingDuo) throws Exception{
        System.err.println("MyPage Controller @Post");
        RedisMyPage testDto = matchingService.saveDuoMatchingToMyPage(matchingDuo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(testDto)
                );
    }

    @PostMapping("/myPage/colosseum")
    @RedisOperation
    public ResponseEntity<APIDataResponse<RedisMyPage>> createColosseumMyPage(@RequestBody MatchingDTO.RequestColosseum matchingColosseum) throws Exception{
        System.err.println("MyPage Controller @Post");
        RedisMyPage testDto = matchingService.saveColosseumMatchingToMyPage(matchingColosseum);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(testDto)
                );
    }

    @DeleteMapping("/myPage/{myPageUUID}")
    @Operation(
            summary = "단일 삭제",
            description = "[삭제] 해당 UUID 키 값 마이페이지 엔티티 삭제\n[개발자용] 마이페이지 보드 타겟 고유 엔티티 삭제."
    )
    public ResponseEntity<APIDataResponse> deleteMyPage(@PathVariable UUID myPageUUID) throws Exception{
        matchingService.deleteMyPage(myPageUUID);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.empty()
                );
    }

    @DeleteMapping("/myPage")
    @Operation(
            summary = "전체 삭제",
            description = "[삭제] Redis 안 모든 마이페이지 엔티티 삭제 요청\n[개발자용] 백엔드 테스트용. 프론트에서 쓰지 말 것을 강력히 권장."
    )
    public ResponseEntity<APIDataResponse> deleteAllMyPage ()throws Exception{
        matchingService.deleteAllMyPage();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.empty()
                );
    }
}