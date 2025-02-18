package com.tools.seoultech.timoproject.chat.repository;

import com.tools.seoultech.timoproject.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    boolean existsById(Long roomId);
}
