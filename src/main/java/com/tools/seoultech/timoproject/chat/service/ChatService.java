package com.tools.seoultech.timoproject.chat.service;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.Message;
import com.tools.seoultech.timoproject.chat.dto.ChatMessageDTO;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
import com.tools.seoultech.timoproject.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    // 내가 속한 room 목록 조회

    // 채팅방 생성
    public ChatRoom createChatRoom(String roomName) {
        ChatRoom chatRoom = ChatRoom.createRoom(roomName);

        return chatRoomRepository.save(chatRoom);
    }

    // 메시지 저장
    public void saveMessage(ChatMessageDTO chatMessageDTO) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomName(chatMessageDTO.room())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        Message message = Message.createMessage(chatRoom, chatMessageDTO.senderId(), chatMessageDTO.content());
        messageRepository.save(message);
        log.info("메시지 저장 완료: content - {}, senderId - {}", message, chatMessageDTO.senderId());
    }


    // 이전 메시지 조회


}
