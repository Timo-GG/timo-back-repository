package com.tools.seoultech.timoproject.constant;


import com.tools.seoultech.timoproject.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

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
    API_ACCESS_ERROR(6, HttpStatus.INTERNAL_SERVER_ERROR, "RIOT API ACCESS error");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.message + " : " + e.getMessage());
    }
    public String getMessage(String message) {
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
