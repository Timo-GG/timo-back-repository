package com.tools.seoultech.timoproject.chat.repository;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findById(Long chatRoomId);


    Optional<ChatRoom> findByMatchId(String matchId);

    @Query("""
    SELECT cr FROM ChatRoom cr
    JOIN cr.chatRoomMembers m1
    JOIN cr.chatRoomMembers m2
    WHERE m1.member.id = :id1 AND m2.member.id = :id2 AND cr.isTerminated = false
""")
    Optional<ChatRoom> findRoomByMembers(@Param("id1") Long id1, @Param("id2") Long id2);
}
