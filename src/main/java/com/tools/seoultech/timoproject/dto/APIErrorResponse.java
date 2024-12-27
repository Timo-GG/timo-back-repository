package com.tools.seoultech.timoproject.dto;

import com.tools.seoultech.timoproject.constant.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class APIErrorResponse {
    private final Boolean success;
    private final Integer errorCode;
    private final String message;

    public static APIErrorResponse of(Boolean success, Integer errorCode, String message){
        return new APIErrorResponse(success, errorCode, message);
    }
    public static APIErrorResponse of(Boolean succeess, ErrorCode error){
        return new APIErrorResponse(succeess, error.getCode(), error.getMessage());
    }
    public static APIErrorResponse of(Boolean success,ErrorCode errorCode, Exception e){
        return new APIErrorResponse(success, errorCode.getCode(), errorCode.getMessage(e));
    }
    public static APIErrorResponse of(Boolean success, ErrorCode errorCode, String message){
        return new APIErrorResponse(success, errorCode.getCode(), errorCode.getMessage(message));
    }
}

