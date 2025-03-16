package com.tools.seoultech.timoproject.chat.service;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.domain.Message;
import com.tools.seoultech.timoproject.chat.dto.ChatMessageDTO;
import com.tools.seoultech.timoproject.chat.dto.ChatRoomResponse;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomMemberRepository;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
import com.tools.seoultech.timoproject.chat.repository.MessageRepository;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
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
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberRepository memberRepository;


    // 메시지 저장
    @Transactional
    public ChatMessageDTO saveMessage(ChatMessageDTO chatMessageDTO) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDTO.roomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        // 1) 메시지 엔티티 생성 & DB 저장
        Message message = Message.createMessage(chatRoom, chatMessageDTO.senderId(), chatMessageDTO.content());
        messageRepository.save(message);

        // 2) 다른 멤버들 unreadCount 증가
        List<ChatRoomMember> members = chatRoomMemberRepository.findByChatRoom(chatRoom);
        for (ChatRoomMember member : members) {
            if (!member.getMember().getId().equals(chatMessageDTO.senderId())) {
                member.increaseUnreadCount();
            }
        }
        chatRoomMemberRepository.saveAll(members);

        // 3) 채팅방의 lastMessage 갱신
        chatRoom.updateLastMessage(message);
        chatRoomRepository.save(chatRoom);

        log.info("메시지 저장 완료: content - {}, senderId - {}", message, chatMessageDTO.senderId());

        // 4) DB에서 생성된 message.getId()를 포함해 DTO로 변환해서 반환
        return ChatMessageDTO.builder()
                .messageId(message.getId())                 // DB에서 생성된 PK
                .roomId(chatRoom.getId())    // roomName
                .senderId(message.getSenderId())      // 보낸 사람 ID
                .content(message.getContent())        // 메시지 내용
                .build();
    }


    // 이전 메시지 조회
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getMessages(Long roomId, int page, int size) {
        // PageRequest로 페이징, 오래된 순 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by("regDate").ascending());

        Page<Message> messagePage = messageRepository.findByChatRoom_Id(roomId, pageable);

        // Message -> ChatMessageDTO 변환
        return messagePage.stream()
                .map(m -> new ChatMessageDTO(
                        m.getId(),
                        m.getChatRoom().getId(),
                        m.getSenderId(),
                        m.getContent()
                ))
                .collect(Collectors.toList());
    }

    // 안 읽은 메시지 개수 조회
    public int getUnreadCount(Long memberId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoom.getId(), memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방 멤버입니다."));

        return chatRoomMember.getUnreadCount();

    }


    public ChatRoomResponse getChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        return ChatRoomResponse.of(chatRoom);
    }

    @Transactional
    public void joinRoom(Long memberId, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방"));

        // 중복 체크
        ChatRoomMember existing = chatRoomMemberRepository.findByChatRoomIdAndMemberId(roomId, memberId)
                .orElse(null);
        if (existing != null) {
            throw new IllegalArgumentException("이미 참여한 방입니다.");
        }

        Member member = memberRepository.findById(memberId).orElseThrow();
        ChatRoomMember newMember = ChatRoomMember.createChatRoomMember(chatRoom, member);
        chatRoomMemberRepository.save(newMember);

    }

    @Transactional
    public void leaveRoom(Long memberId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방"));

        ChatRoomMember chatRoomMember = chatRoomMemberRepository
                .findByChatRoomIdAndMemberId(chatRoom.getId(), memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 이 방에 없습니다."));

        // 실제로 delete하지 않고, isLeft = true 설정
        chatRoomMember.leave();
        chatRoomMemberRepository.save(chatRoomMember);

        // (옵션) 방 인원이 전부 나갔다면, 방 자체도 isTerminated = true
        boolean allLeft = chatRoomMemberRepository.findByChatRoom(chatRoom).stream()
                .allMatch(ChatRoomMember::isLeft);
        if (allLeft) {
            chatRoom.terminate();
            chatRoomRepository.save(chatRoom);
        }
    }

    public List<ChatRoomMember> findActiveMembers(Long roomId) {
        return chatRoomMemberRepository.findByChatRoomId(roomId)
                .stream()
                .filter(m -> !m.isLeft())
                .collect(Collectors.toList());
    }

    @Transactional
    public void createChatRoomForMatch(String matchId, Long member1Id, Long member2Id) {
        // 예: 채팅방 이름을 "member1Id_member2Id" 형태로 생성
        String chatRoomName = member1Id + "_" + member2Id;

        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.createRoom(chatRoomName, matchId);
        chatRoomRepository.save(chatRoom);

        // 채팅방 멤버 추가
        Member user1 = memberRepository.findById(member1Id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원: " + member1Id));
        Member user2 = memberRepository.findById(member2Id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원: " + member2Id));

        ChatRoomMember chatRoomMember1 = ChatRoomMember.createChatRoomMember(chatRoom, user1);
        ChatRoomMember chatRoomMember2 = ChatRoomMember.createChatRoomMember(chatRoom, user2);

        chatRoomMemberRepository.save(chatRoomMember1);
        chatRoomMemberRepository.save(chatRoomMember2);

        log.info("✅ 채팅방 생성 및 멤버 가입 완료. matchId={}, chatRoomName={}", matchId, chatRoomName);
    }

    @Transactional(readOnly = true)
    public ChatRoom findChatRoomByMatchId(String matchId) {
        // ChatRoom 엔티티에 matchId 필드가 있다고 가정
         return chatRoomRepository.findByMatchId(matchId).orElse(null);
    }

    @Transactional
    public void terminateChat(String matchId) {
        ChatRoom chatRoom = chatRoomRepository.findByMatchId(matchId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION));
        if (!chatRoom.isTerminated()) {
            chatRoom.terminate();
        }
        chatRoomRepository.save(chatRoom);
    }

    public Long findActiveChatRoomIdForMember(Long memberId) {
        return chatRoomMemberRepository.findFirstByMember_IdAndIsLeftFalse(memberId)
                .map(ChatRoomMember::getChatRoom)
                .filter(chatRoom -> !chatRoom.isTerminated())
                .map(ChatRoom::getId)
                .orElseThrow(() -> new IllegalStateException("활성 채팅방이 존재하지 않습니다."));
    }
}
