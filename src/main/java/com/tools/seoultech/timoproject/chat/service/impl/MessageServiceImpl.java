package com.tools.seoultech.timoproject.chat.service.impl;

import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.chat.repository.MessageRepository;
import com.tools.seoultech.timoproject.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public List<Message> getMessage(String room) {
        return messageRepository.findAllByRoom(room);
    }

    @Override
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }
}
