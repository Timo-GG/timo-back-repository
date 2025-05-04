package com.tools.seoultech.timoproject.post.controller.exceptionhandler;

import com.tools.seoultech.timoproject.global.APIErrorResponse;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.post.controller.PostApiController;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice(basePackageClasses = PostApiController.class)
public class PostExceptionHandler {
    @ExceptionHandler(value = { MethodArgumentNotValidException.class})
    public ResponseEntity<APIErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorList = e.getFieldErrors().stream()
                .map(error -> {
                    String format = "[Error Field]: %s, [Error Value]: %s], [Error Message]: %s\n";
                    return String.format(
                            format,
                            error.getField(), error.getRejectedValue(), error.getDefaultMessage());
                }).toString();
        var data = APIDataResponse.of(
                false,
                APIErrorResponse.of(false, ErrorCode.VALIDATION_ERROR, e.getMessage() + "\n" + errorList));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
    }
}
