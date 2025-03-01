package com.tools.seoultech.timoproject.chat.service;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.Message;
import com.tools.seoultech.timoproject.chat.dto.ChatMessageDTO;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
import com.tools.seoultech.timoproject.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional
    public void saveMessage(ChatMessageDTO chatMessageDTO) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomName(chatMessageDTO.room())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        Message message = Message.createMessage(chatRoom, chatMessageDTO.senderId(), chatMessageDTO.content());

        messageRepository.save(message);

        chatRoom.updateLastMessage(message);
        log.info("메시지 저장 완료: content - {}, senderId - {}", message, chatMessageDTO.senderId());
    }

    // 이전 메시지 조회
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getMessages(String roomName, int page, int size) {
        // PageRequest로 페이징, 오래된 순 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by("regDate").ascending());

        Page<Message> messagePage = messageRepository.findByChatRoom_ChatRoomName(roomName, pageable);

        // Message -> ChatMessageDTO 변환
        return messagePage.stream()
                .map(m -> new ChatMessageDTO(
                        m.getChatRoom().getChatRoomName(),
                        m.getSenderId(),
                        m.getContent()
                ))
                .collect(Collectors.toList());
    }


}
