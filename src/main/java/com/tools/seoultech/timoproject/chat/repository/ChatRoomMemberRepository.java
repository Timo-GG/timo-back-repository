package com.tools.seoultech.timoproject.chat.repository;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findByChatRoom(ChatRoom chatRoom);

    Optional<ChatRoomMember> findByChatRoomIdAndMember_MemberId(Long chatRoomId, Long memberId);

    Collection<ChatRoomMember> findByChatRoomId(Long roomId);

    @Query("SELECT crm FROM ChatRoomMember crm " +
            "JOIN FETCH crm.chatRoom cr " +
            "JOIN FETCH crm.member m " + // Member 정보도 함께 조회하여 N+1 방지
            "WHERE cr.id IN (SELECT crm2.chatRoom.id FROM ChatRoomMember crm2 WHERE crm2.member.id = :memberId)")
    List<ChatRoomMember> findByMemberIdWithDuo(@Param("memberId") Long memberId);

    // 현재 활성화된(참여중이고 종료되지 않은) 채팅방 조회
    Optional<ChatRoomMember> findFirstByMember_MemberIdAndIsLeftFalse(Long memberId);

    List<ChatRoomMember> findByMember_MemberIdAndIsLeftFalse(Long memberId);

    @Query("SELECT crm FROM ChatRoomMember crm " +
            "JOIN FETCH crm.chatRoom cr " +
            "JOIN FETCH crm.member m " +
            "WHERE m.memberId = :memberId AND crm.isLeft = false")
    List<ChatRoomMember> findByMember_MemberIdWithChatRoomAndMember(@Param("memberId") Long memberId);

    @Query("SELECT crm FROM ChatRoomMember crm " +
            "JOIN FETCH crm.member m " +
            "WHERE crm.chatRoom.id = :chatRoomId AND m.memberId != :excludeMemberId " +
            "AND crm.isLeft = false")
    Optional<ChatRoomMember> findByChatRoomIdAndMember_MemberIdNotWithMember(
            @Param("chatRoomId") Long chatRoomId,
            @Param("excludeMemberId") Long excludeMemberId);
}
