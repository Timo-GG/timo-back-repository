package com.tools.seoultech.timoproject.global.error;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class ViewExceptionHandler {
    @ExceptionHandler
    public ModelAndView handleException(GeneralException e) {
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus httpStatus = errorCode.isClientSideError() ?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        log.info("익셉션 핸들러에서 예외처리");
        log.error(e.getMessage(), e);
        return new ModelAndView(
                "error",
                Map.of(
                "errorCode", errorCode.getCode(),
                "httpStatus", errorCode.getHttpStatus(),
                "message", errorCode.getMessage()
        ));
    }

    @ExceptionHandler
    public ModelAndView handleException(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log.info("익셉션 핸들러에서 예외 처리.");
        log.error(e.getMessage(), e);
        return new ModelAndView(
                "error",
                Map.of(
                        "errorCode", errorCode.getCode(),
                        "httpStatus", errorCode.getHttpStatus(),
                        "message", errorCode.getMessage()
        ));
    }
}
