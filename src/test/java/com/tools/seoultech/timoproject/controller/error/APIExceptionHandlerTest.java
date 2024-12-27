package com.tools.seoultech.timoproject.controller.error;

import com.tools.seoultech.timoproject.constant.ErrorCode;
import com.tools.seoultech.timoproject.dto.APIErrorResponse;
import com.tools.seoultech.timoproject.dto.AccountDto;
import com.tools.seoultech.timoproject.exception.RiotAPIException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[API Exception Handler]")
class APIExceptionHandlerTest {
    private APIExceptionHandler handler;
    private WebRequest request;

    @BeforeEach
    void setUp(){
        handler = new APIExceptionHandler();
        request = new ServletWebRequest(new MockHttpServletRequest());
    }

    @DisplayName("ConstraintViolation 오류 예외처리 검증")
    @Test
    void givenConstraintViolationException_whenHandlingAPIException_thenReturnResponseEntity() {
        //given
        ConstraintViolationException e = new ConstraintViolationException(Set.of());
        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        String equals = APIErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)).toString();

        //when
        ResponseEntity<Object> response = handler.handleConstraintViolationException(e, request);

        //then
        assertThat(response)
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.BAD_REQUEST)
//                .hasFieldOrPropertyWithValue("body", APIErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)));
                .satisfies( resp -> {
                    assertThat(resp.getBody().toString()).isEqualTo(equals);
                });

    }
    @DisplayName("RiotAPIException 오류 예외처리 검증")
    @Test
    void givenRiotAPIException_whenHandlingAPIException_thenReturnResponseEntity() {
        //given
        RiotAPIException e = new RiotAPIException("test");
        ErrorCode errorCode = ErrorCode.API_ACCESS_ERROR;

        //when
        ResponseEntity<Object> response = handler.handleRiotAPIException(e, request);
        System.err.println(response);

        //then
        assertThat(response)
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.INTERNAL_SERVER_ERROR)
//                .hasFieldOrPropertyWithValue("body", APIErrorResponse.of(false, ErrorCode.API_ACCESS_ERROR, e.getMessage()));
                .satisfies( resp -> {
                    assertThat(resp.getBody().toString())
                            .isEqualTo(APIErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)).toString());
                });
    }
}
