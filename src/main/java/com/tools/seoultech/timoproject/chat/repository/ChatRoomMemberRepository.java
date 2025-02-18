package com.tools.seoultech.timoproject.chat.repository;

import com.tools.seoultech.timoproject.chat.model.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    Optional<ChatRoomMember> findByMemberIdAndChatRoomId(long memberId, long chatRoomId);
    boolean existsByMemberIdAndChatRoomId(long memberId, long chatRoomId);

    @Query("select crm from ChatRoomMember crm "
            + "join fetch crm.member m "
            + "where crm.chatRoom.id = :chatRoomId")
    List<ChatRoomMember> findByChatRoomIdWithMember(long chatRoomId);
}
