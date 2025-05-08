package com.tools.seoultech.timoproject.matching.controller;

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
    @Operation(
            summary = "단일 조회",
            description = "[조회] UUID 키 값으로 단일 MyPage 엔티티 조회\n[개발자용] 마이페이지 보드 고유 엔티티값 조회. "
    )
    public ResponseEntity<APIDataResponse<MyPageDTO.ResponseMyPage>> getMyPage(@PathVariable UUID myPageUUID) throws Exception{
        MyPageDTO.ResponseMyPage testDto = matchingService.getMyPage(myPageUUID);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(testDto)
                );
    }
    @GetMapping("/myPage/category/{category}")
    @Operation(
            summary = "카테고리별 조회",
            description = "[개발자용] 마이페이지 보드 공통 카테고리 그룹 엔티티 조회."
    )
    public ResponseEntity<APIDataResponse<List<MyPageDTO.ResponseMyPage>>> getMyPage(@PathVariable MatchingCategory category) throws Exception{
        List<MyPageDTO.ResponseMyPage> testDtoList = matchingService.getMyPage(category);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(testDtoList)
                );
    }
    @PostMapping("/myPage")
    @Operation(
            summary = "매칭 생성",
            description = "[생성] 매칭 이벤트 발생시 마이페이지 엔티티 생성\n[개발자용] 마이페이지 보드 사용자간 연결."
    )
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
