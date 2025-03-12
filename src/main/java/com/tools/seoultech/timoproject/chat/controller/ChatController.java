package com.tools.seoultech.timoproject.chat.controller;


import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.dto.ChatMessageDTO;
import com.tools.seoultech.timoproject.chat.dto.ChatRoomResponse;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/rooms")
    public APIDataResponse<ChatRoomResponse> getRoom(@RequestParam Long roomId) {
        return APIDataResponse.of(chatService.getChatRoom(roomId));
    }

    @GetMapping("/rooms/{roomId}/messages")
    public List<ChatMessageDTO> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page
    ) {
        // 예: 페이지네이션(한 페이지당 20개) 사용
        return chatService.getMessages(roomId, page, 20);
    }

    @GetMapping("/rooms/{roomName}/unread")
    public APIDataResponse<?> getUnreadCount(@CurrentMemberId Long memberId, @PathVariable Long roomId) {
        return APIDataResponse.of(chatService.getUnreadCount(memberId, roomId));
    }

    @PostMapping("/rooms/{roomId}/join")
    public APIDataResponse<?> joinRoom(@RequestParam Long memberId, @PathVariable Long roomId) {
        chatService.joinRoom(memberId, roomId);
        return APIDataResponse.of("방 참여 완료");
    }

    @PostMapping("/terminate")
    public APIDataResponse<?> terminateRoom(@RequestParam String matchId) {
        chatService.terminateChat(matchId);
        return APIDataResponse.of("방 종료 완료");
    }

    @PostMapping("/test")
    public APIDataResponse<?> test() {
        chatService.createChatRoomForMatch("test", 1L, 2L);
        return APIDataResponse.of("멤버 1, 멤버 2, 채팅방 이름 : test 생성");
    }
}
