package com.tools.seoultech.timoproject.chat.service;

import com.tools.seoultech.timoproject.chat.model.ChatRoom;
import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.member.domain.Member;

import java.util.List;

public interface ChatRoomService {

    ChatRoom createChatRoom();

    ChatRoom getById(Long roomId);

    void validateUserInRoom(Member member, ChatRoom room);

    Long getRecentMessageId(long memberId, long chatRoomId);

    void chatRoomJoin(long chatRoomId, Member member);

    void chatRoomLeave(long chatRoomId, Member member);

}
