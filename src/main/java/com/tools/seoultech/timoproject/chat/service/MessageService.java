package com.tools.seoultech.timoproject.chat.service;


import com.tools.seoultech.timoproject.chat.model.Message;

import java.util.List;

public interface MessageService {

    List<Message> getMessage(String room);

    Message saveMessage(Message message);

    Message getById(Long messageId);
}
