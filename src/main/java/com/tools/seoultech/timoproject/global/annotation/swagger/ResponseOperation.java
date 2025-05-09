package com.tools.seoultech.timoproject.global.annotation.swagger;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Duo MyPage 생성",
        description = "Duo 매칭 요청을 기반으로 MyPage를 생성합니다.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Duo MyPage 생성 성공",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                oneOf = {BoardDTO.ResponseDuo.class, BoardDTO.ResponseColosseum.class}, // 반환되는 타입을 RedisMyPage로 설정
                                                discriminatorProperty = "matchingCategory" // JsonTypeInfo 없어서 작동 X,matchingCategory로 구분
                                        )
                                ),
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                oneOf = {UserDTO.ResponseDuo.class, UserDTO.ResponseColosseum.class}, // 반환되는 타입을 RedisMyPage로 설정
                                                discriminatorProperty = "matchingCategory" // matchingCategory로 구분
                                        )
                                )
                        }
                )
        }
)
public @interface ResponseOperation {
}
