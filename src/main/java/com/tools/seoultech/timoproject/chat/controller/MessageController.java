package com.tools.seoultech.timoproject.chat.controller;


import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @CrossOrigin
    @GetMapping("/{room}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(messageService.getMessage(room));
    }


}
