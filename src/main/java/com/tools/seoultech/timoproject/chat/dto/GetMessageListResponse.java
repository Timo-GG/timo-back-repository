package com.tools.seoultech.timoproject.chat.dto;

public record GetMessageListResponse(
    PageResult<MessageResponse> pageResult
) {

    public static GetMessageListResponse from(PageResult<MessageResponse> pageResult) {
        return new GetMessageListResponse(pageResult);
    }
}
