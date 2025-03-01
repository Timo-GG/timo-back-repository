package com.tools.seoultech.timoproject.chat.controller;


import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.dto.ChatMessageDTO;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/rooms")
    public APIDataResponse<?> createRoom(@RequestParam String roomName) {
        ChatRoom chatRoom = chatService.createChatRoom(roomName);

        return APIDataResponse.of(chatRoom);
    }

    @GetMapping("/rooms/{roomName}/messages")
    public List<ChatMessageDTO> getMessages(
            @PathVariable String roomName,
            @RequestParam(defaultValue = "0") int page
    ) {
        // 예: 페이지네이션(한 페이지당 20개) 사용
        return chatService.getMessages(roomName, page, 20);
    }
}
