package com.tools.seoultech.timoproject.global.exception;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private final ErrorCode errorCode;

    public GeneralException() {
        super(ErrorCode.INTERNAL_ERROR.getMessage());
        this.errorCode = ErrorCode.INTERNAL_ERROR;
    }
    public GeneralException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public GeneralException(String message) {
        super(message);
        this.errorCode = ErrorCode.INTERNAL_ERROR;
    }
    public GeneralException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
