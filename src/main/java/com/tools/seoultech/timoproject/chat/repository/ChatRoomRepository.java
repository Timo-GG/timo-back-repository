package com.tools.seoultech.timoproject.chat.repository;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByChatRoomName(String chatRoomName);
}
