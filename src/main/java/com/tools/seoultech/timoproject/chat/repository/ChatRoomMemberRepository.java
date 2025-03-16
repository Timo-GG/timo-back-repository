package com.tools.seoultech.timoproject.chat.repository;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findByChatRoom(ChatRoom chatRoom);

    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    Collection<ChatRoomMember> findByChatRoomId(Long roomId);

    List<ChatRoomMember> findByMemberId(Long memberId);

    // 현재 활성화된(참여중이고 종료되지 않은) 채팅방 조회
    Optional<ChatRoomMember> findFirstByMember_IdAndIsLeftFalse(Long memberId);
}
