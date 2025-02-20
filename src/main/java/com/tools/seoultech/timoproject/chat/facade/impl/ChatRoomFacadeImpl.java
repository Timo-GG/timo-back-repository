package com.tools.seoultech.timoproject.chat.facade.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.tools.seoultech.timoproject.chat.facade.ChatRoomFacade;
import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.chat.model.MessageType;
import com.tools.seoultech.timoproject.chat.dto.MessageResponse;
import com.tools.seoultech.timoproject.chat.dto.PageResult;
import com.tools.seoultech.timoproject.chat.service.ChatRoomService;
import com.tools.seoultech.timoproject.chat.service.MessageService;
import com.tools.seoultech.timoproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class ChatRoomFacadeImpl implements ChatRoomFacade {
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final MemberService memberService;

    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    @Transactional
    public MessageResponse sendTextMessage(SocketIOClient client, long memberId, long roomId, String content) {
        var member = memberService.getById(memberId);
        var room = chatRoomService.getById(roomId);
        var message = messageService.saveMessage(
                Message.newInstance(room, member, MessageType.TEXT, content));
        chatRoomService.validateMemberInRoom(member, room);
        //todo : update last checked message

        return MessageResponse.from(message);
    }

    @Override
    @Transactional
    public void receiveMessage(SocketIOClient client, long memberId, long roomId, long messageId) {
        var member = memberService.getById(memberId);
        var room = chatRoomService.getById(roomId);
        var message = messageService.getById(messageId);
        chatRoomService.validateMemberInRoom(member, room);
        //todo : update last checked message

    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<MessageResponse> getMessages(long memberId, long roomId, Long lastMessageId) {
        var member = memberService.getById(memberId);
        var room = chatRoomService.getById(roomId);
        Slice<Message> slice = chatRoomService.getBeforeMessagesByPaging(
                room.getId(), lastMessageId, DEFAULT_PAGE_SIZE);
        chatRoomService.validateMemberInRoom(member, room);
        // todo : update last checked message

        return PageResult.of(
                slice.getContent()
                        .stream()
                        .map(MessageResponse::from)
                        .collect(toList()),slice.hasNext()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Long getRecentMessageId(long memberId, long roomId) {
        var member = memberService.getById(memberId);
        var room = chatRoomService.getById(roomId);
        chatRoomService.validateMemberInRoom(member, room);
        return chatRoomService.getRecentMessageId(member.getId(), room.getId());
    }
}
