package com.tools.seoultech.timoproject.chat.model.dto;

public record GetMessageListResponse(
    PageResult<MessageResponse> pageResult
) {
}
