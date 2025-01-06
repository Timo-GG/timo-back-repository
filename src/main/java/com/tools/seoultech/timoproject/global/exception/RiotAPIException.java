package com.tools.seoultech.timoproject.global.exception;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import lombok.Getter;

@Getter
public class RiotAPIException extends RuntimeException {
    private final ErrorCode errorCode;
    public RiotAPIException(){
        super();
        this.errorCode = ErrorCode.API_ACCESS_ERROR;
    }
    public RiotAPIException(String message){
        super(message);
        this.errorCode = ErrorCode.API_ACCESS_ERROR;
    }
    public RiotAPIException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
