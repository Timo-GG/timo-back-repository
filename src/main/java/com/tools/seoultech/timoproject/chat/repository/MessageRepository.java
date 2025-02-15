package com.tools.seoultech.timoproject.chat.repository;

import com.tools.seoultech.timoproject.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByRoom(String room);
}
