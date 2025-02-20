package com.tools.seoultech.timoproject.chat.service;

import com.tools.seoultech.timoproject.chat.model.ChatRoom;
import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.member.domain.Member;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatRoomService {

    ChatRoom createChatRoom();

    ChatRoom getById(Long roomId);

    void validateMemberInRoom(Member member, ChatRoom room);

    Long getRecentMessageId(long memberId, long chatRoomId);

    void chatRoomJoin(long chatRoomId, Member member);

    void chatRoomLeave(long chatRoomId, Member member);

    Slice<Message> getBeforeMessagesByPaging(long roomId, Long messageId, int size);

}
