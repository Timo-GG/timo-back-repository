package com.tools.seoultech.timoproject.riot.dto;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.APIErrorResponse;
import lombok.Getter;

@Getter
public class APIDataResponse<T> extends APIErrorResponse {
    private final T data;

    private APIDataResponse(T data) {
        super(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }
<<<<<<< HEAD
=======
    private APIDataResponse(boolean success, APIErrorResponse error) {
        super(success, error.getErrorCode(), error.getMessage());
        this.data = null;
    }
>>>>>>> #12-crud-repository

    public static <T> APIDataResponse<T> of(T data) {
        return new APIDataResponse<>(data);
    }

    public static <T> APIDataResponse<T> empty() {
        return new APIDataResponse<>(null);
    }
<<<<<<< HEAD
=======
    public static <T> APIDataResponse<T> of(boolean success, APIErrorResponse error) {
        return new APIDataResponse<>(success, error);
    }
>>>>>>> #12-crud-repository
}
