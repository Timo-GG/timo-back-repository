package com.tools.seoultech.timoproject.chat.repository;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findByChatRoom(ChatRoom chatRoom);

    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
}
