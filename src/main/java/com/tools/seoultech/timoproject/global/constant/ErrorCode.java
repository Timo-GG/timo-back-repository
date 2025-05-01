package com.tools.seoultech.timoproject.global.constant;


import com.tools.seoultech.timoproject.global.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    OK(0, HttpStatus.OK, "Ok"),

    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "Bad Request"),
    SPRING_BAD_REQUEST(10001, HttpStatus.BAD_REQUEST, "Spring detected Bad Request"),
    VALIDATION_ERROR(10002, HttpStatus.BAD_REQUEST, "Validation error"),

    INTERNAL_ERROR(3, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error"),
    SPRING_INTERNAL_ERROR(4,HttpStatus.INTERNAL_SERVER_ERROR, "Spring detected Internal Error"),
    DATA_ACCESS_ERROR(5, HttpStatus.INTERNAL_SERVER_ERROR, "Data Access error"),
    API_ACCESS_ERROR(6, HttpStatus.INTERNAL_SERVER_ERROR, "RIOT API ACCESS error"),

    /**
     * auth. code prefix: 600번대
     */
    UNAUTHORIZED_EXCEPTION(600, HttpStatus.UNAUTHORIZED , "인증되지 않은 사용자입니다."),
    EXPIRED_ACCESS_TOKEN_EXCEPTION(601, HttpStatus.UNAUTHORIZED,  "만료된 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN_EXCEPTION(602, HttpStatus.UNAUTHORIZED,  "만료된 리프레시 토큰입니다."),
    INVALID_ACCESS_TOKEN_EXCEPTION(603, HttpStatus.UNAUTHORIZED,  "유효하지 않은 엑세스 토큰입니다."),
    INVALID_REFRESH_TOKEN_EXCEPTION(604, HttpStatus.UNAUTHORIZED,  "유효하지 않은 리프레시 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN_EXCEPTION(605, HttpStatus.UNAUTHORIZED,  "지원하지 않는 JWT 토큰입니다."),
    UNSUPPORTED_SOCIAL_PLATFORM_EXCEPTION(606, HttpStatus.UNAUTHORIZED,  "지원하지 않는 소셜 플랫폼입니다."),
    MEMBER_NOT_FOUND(607, HttpStatus.UNAUTHORIZED,  "존재하지 않는 사용자입니다."),

    /**
     * resource. code prefix: 700번대
     */
    FORBIDDEN_EXCEPTION(700,
            HttpStatus.FORBIDDEN, "리소스에 접근 권한이 없습니다."),

    /**
     * common. code prefix: 800번대
     */
    INTERNAL_SERVER_ERROR(800, HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다"),
    INVALID_INPUT_VALUE(801, HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    NOT_FOUND_RESOURCE_EXCEPTION(802, HttpStatus.NOT_FOUND, "존재하지 않는 데이터입니다."),
    DUPLICATED_RESOURCE_EXCEPTION(803, HttpStatus.CONFLICT, "이미 존재하는 데이터입니다."),
    PARSE_JSON_EXCEPTION(804, HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱 에러가 발생했습니다."),
    UNSUPPORTED_MESSAGE_TYPE_EXCEPTION(805, HttpStatus.BAD_REQUEST, "지원하지 않는 채팅 메세지 타입입니다."),
    NOT_FOUND_HANDLER_EXCEPTION(806, HttpStatus.NOT_FOUND, "지원하지 않는 Api 요청 입니다."),
    NOT_FOUND_USER_EXCEPTION(807, HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    INVALID_ACCESS_EXCEPTION(808, HttpStatus.FORBIDDEN, "잘못된 접근입니다."),
    NOT_FOUND_DUO_EXCEPTION(809, HttpStatus.NOT_FOUND, "존재하지 않는 듀오입니다."),
    NOT_FOUND_CHATROOM_EXCEPTION(810, HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다."),
    CHATROOM_NOT_TERMINATED_EXCEPTION(811, HttpStatus.BAD_REQUEST, "채팅방이 종료되지 않았습니다."),
    DUPLICATE_RATING_EXCEPTION(812, HttpStatus.CONFLICT, "이미 평점을 제출한 매칭입니다."),
    INVALID_RANKING_INFO(813, HttpStatus.BAD_REQUEST, "소환사 계정 또는 학교 계정 정보가 누락되었습니다."),

    /**
     * my-setting. code prefix: 900번대
     */

    ALREADY_USED_RIOT_ACCOUNT(900, HttpStatus.BAD_REQUEST, "이미 사용중인 소환사 계정입니다."),
    ALREADY_USED_USERNAME(901, HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    ALREADY_USED_UNIV_ACCOUNT(902, HttpStatus.BAD_REQUEST, "이미 사용중인 학교 계정입니다."),
    UNIV_ALREADY_VERIFIED(903, HttpStatus.CREATED, "이미 인증이 완료된 사용자입니다."),
    RANKING_NOT_FOUND(904, HttpStatus.NOT_FOUND, "랭킹 정보가 존재하지 않습니다."),

    REDIS_RANKING_NOT_FOUND(1000, HttpStatus.NOT_FOUND, "랭킹 정보가 존재하지 않습니다."),
    REDIS_ERROR(1001, HttpStatus.INTERNAL_SERVER_ERROR, "Redis 서버 에러입니다.");


    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
        return this.message;
    }    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(getMessage());
    }
    public boolean isClientSideError() {
        return this.getHttpStatus() == HttpStatus.BAD_REQUEST;
    }
    public boolean isServerSideError() {
        return this.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR;
    }
    public static ErrorCode valueOf(HttpStatus httpStatus) {
        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet( () -> {
                    if (httpStatus.is4xxClientError()) {
                        return ErrorCode.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return ErrorCode.INTERNAL_ERROR;
                    } else throw new GeneralException("httpStatus is does not matching on server ErrorCode");
                });

    }
}
