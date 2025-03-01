package com.tools.seoultech.timoproject.chat.repository;

import com.tools.seoultech.timoproject.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
