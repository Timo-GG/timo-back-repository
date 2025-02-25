package com.tools.seoultech.timoproject.chat.controller;


import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.chat.service.MessageService;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @CrossOrigin
    @GetMapping("/{room}")
    public ResponseEntity<APIDataResponse<List<Message>>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(APIDataResponse.of(messageService.getMessage(room)));
    }

}
