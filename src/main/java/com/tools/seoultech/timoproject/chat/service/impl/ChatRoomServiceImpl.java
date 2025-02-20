package com.tools.seoultech.timoproject.chat.service.impl;

import com.tools.seoultech.timoproject.chat.constant.Constants;
import com.tools.seoultech.timoproject.chat.model.ChatRoom;
import com.tools.seoultech.timoproject.chat.model.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.chat.model.MessageType;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomMemberRepository;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
import com.tools.seoultech.timoproject.chat.repository.MessageRepository;
import com.tools.seoultech.timoproject.chat.service.ChatRoomService;
import com.tools.seoultech.timoproject.chat.service.MessageService;
import com.tools.seoultech.timoproject.member.domain.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MessageService messageService;
    private final MessageRepository messageRepository;


    @Override
    public ChatRoom createChatRoom() {
        return chatRoomRepository.save(ChatRoom.newInstance());
    }
    @Override
    public ChatRoom getById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("ChatRoom not found with id: " + roomId));
    }

    @Override
    public void validateMemberInRoom(Member member, ChatRoom room) {
        if(!chatRoomMemberRepository.existsByMemberIdAndChatRoomId(member.getId(), room.getId())) {
            throw new EntityNotFoundException("Member not found in ChatRoom with id: " + room.getId());
        }

    }

    @Override
    public Long getRecentMessageId(long memberId, long chatRoomId) {
        return chatRoomMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId)
                .map(ChatRoomMember::getLastCheckedMessage)
                .map(Message::getId)
                .orElse(null);
    }

    @Override
    public Slice<Message> getBeforeMessagesByPaging(long roomId, Long messageId, int size){
        return messageId == null ?
                messageRepository.findByChatRoomIdOrderByIdDesc(roomId, PageRequest.of(0, size)) :
                messageRepository.findByRoomIdLessThanIdOrderByIdDesc(roomId, messageId, PageRequest.of(0, size));
    }

    @Override
    public void chatRoomJoin(long chatRoomId, Member member) {
        var chatRoom = getById(chatRoomId);
        var message = Message.newInstance(
                chatRoom,
                member,
                MessageType.JOIN,
                String.format(Constants.DISCONNECT_MESSAGE, member.getNickname()));
        messageService.saveMessage(message);
        chatRoomMemberRepository.save(ChatRoomMember.create(member, chatRoom));
    }

    @Override
    public void chatRoomLeave(long chatRoomId, Member member) {
        var chatRoom = getById(chatRoomId);
        var message = Message.newInstance(
                chatRoom,
                member,
                MessageType.EXIT,
                String.format(Constants.DISCONNECT_MESSAGE, member.getNickname()));
        messageService.saveMessage(message);
        chatRoomMemberRepository
                .findByMemberIdAndChatRoomId(member.getId(), chatRoomId)
                .ifPresent(chatRoomMember -> chatRoomMemberRepository.delete(chatRoomMember));
        ;
    }
}
