package com.tools.seoultech.timoproject.chat.repository;

import com.tools.seoultech.timoproject.chat.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChatRoom_Id(Long roomId, Pageable pageable);

    List<Message> findByChatRoom_IdAndRegDateAfterOrderByRegDateAsc(Long roomId, LocalDateTime sinceTime);
}
