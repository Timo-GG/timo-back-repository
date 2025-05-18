package com.tools.seoultech.timoproject.global.annotation.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//
//@Target({ElementType.METHOD})
//@Retention(RetentionPolicy.RUNTIME)
//@Operation(
//        summary = "Duo MyPage 생성",
//        description = "Duo 매칭 요청을 기반으로 MyPage를 생성합니다.",
//        responses = {
//                @ApiResponse(
//                        responseCode = "200",
//                        description = "Duo MyPage 생성 성공",
//                        content = {
//                                @Content(
//                                        mediaType = "application/json",
//                                        schema = @Schema(
//                                                oneOf = {RedisBoard.Duo.class, RedisBoard.Colosseum.class}, // 반환되는 타입을 RedisMyPage로 설정
//                                                discriminatorProperty = "matchingCategory" // matchingCategory로 구분
//                                        )
//                                ),
//                                @Content(
//                                        mediaType = "application/json",
//                                        schema = @Schema(
//                                                oneOf = {RedisUser.Duo.class, RedisUser.Colosseum.class}, // 반환되는 타입을 RedisMyPage로 설정
//                                                discriminatorProperty = "matchingCategory" // matchingCategory로 구분
//                                        )
//                                )
////                        @Content(mediaType = "application/json", schema = @Schema(implementation = RedisBoard.Duo.class)),
////                        @Content(mediaType = "application/json", schema = @Schema(implementation = RedisUser.Duo.class))
//                        }
//                )
//        }
//)
public @interface RedisOperation {
}
