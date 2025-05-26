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
import com.tools.seoultech.timoproject.member.MemberRepository;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberRepository memberRepository;

    /**
     * 메시지 저장
     */
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
            if (!member.getMember().getMemberId().equals(chatMessageDTO.senderId())) {
                member.increaseUnreadCount();
            }
        }
        chatRoomMemberRepository.saveAll(members);

        // 3) 채팅방의 lastMessage 갱신
        chatRoom.updateLastMessage(message);
        chatRoomRepository.save(chatRoom);

        log.info("메시지 저장 완료: content - {}, senderId - {}", message.getContent(), message.getSenderId());

        // 4) DTO 변환 후 반환
        return ChatMessageDTO.builder()
                .messageId(message.getId())
                .roomId(chatRoom.getId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .build();
    }

    /**
     * 메시지 조회 (페이징)
     */
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getMessages(Long roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "regDate"));
        Page<Message> messagePage = messageRepository.findByChatRoom_Id(roomId, pageable);

        return messagePage.stream()
                .map(m -> ChatMessageDTO.builder()
                        .messageId(m.getId())
                        .roomId(m.getChatRoom().getId())
                        .senderId(m.getSenderId())
                        .content(m.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 안 읽은 메시지 개수 조회
     */
    @Transactional(readOnly = true)
    public int getUnreadCount(Long memberId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMember_MemberId(chatRoom.getId(), memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방 멤버입니다."));

        return chatRoomMember.getUnreadCount();
    }

    /**
     * 사용자의 채팅방 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRoomsByMemberId(Long memberId) {
        List<ChatRoomMember> memberships = chatRoomMemberRepository
                .findByMember_MemberIdWithChatRoomAndMember(memberId);

        return memberships.stream()
                .map(currentMember -> {
                    ChatRoom chatRoom = currentMember.getChatRoom();

                    ChatRoomMember opponent = chatRoomMemberRepository
                            .findByChatRoomIdAndMember_MemberIdNotWithMember(chatRoom.getId(), memberId)
                            .orElse(null);

                    return ChatRoomResponse.of(chatRoom, currentMember, opponent);
                })
                .collect(Collectors.toList());
    }

    /**
     * 채팅방 입장
     */
    @Transactional
    public void joinRoom(Long memberId, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방"));

        ChatRoomMember existing = chatRoomMemberRepository.findByChatRoomIdAndMember_MemberId(roomId, memberId)
                .orElse(null);
        if (existing != null) {
            log.info("이미 참여한 채팅방입니다. roomId={}, memberId={}", roomId, memberId);
            return;
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원: " + memberId));

        ChatRoomMember newMember = ChatRoomMember.createChatRoomMember(chatRoom, member);
        chatRoomMemberRepository.save(newMember);

        log.info("채팅방 입장 완료. roomId={}, memberId={}", roomId, memberId);
    }

    /**
     * 채팅방 나가기
     */
    @Transactional
    public void leaveRoom(Long memberId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방"));

        ChatRoomMember chatRoomMember = chatRoomMemberRepository
                .findByChatRoomIdAndMember_MemberId(chatRoom.getId(), memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 이 방에 없습니다."));

        chatRoomMember.leave();
        chatRoomMemberRepository.save(chatRoomMember);

        // 모든 멤버가 나갔는지 확인
        boolean allLeft = chatRoomMemberRepository.findByChatRoom(chatRoom).stream()
                .allMatch(ChatRoomMember::isLeft);

        if (allLeft) {
            chatRoom.terminate();
            chatRoomRepository.save(chatRoom);
            log.info("모든 멤버가 나가서 채팅방을 종료합니다. roomId={}", chatRoomId);
        }

        log.info("채팅방 나가기 완료. roomId={}, memberId={}", chatRoomId, memberId);
    }

    /**
     * 활성 멤버 조회
     */
    @Transactional(readOnly = true)
    public List<ChatRoomMember> findActiveMembers(Long roomId) {
        return chatRoomMemberRepository.findByChatRoomId(roomId)
                .stream()
                .filter(m -> !m.isLeft())
                .collect(Collectors.toList());
    }

    /**
     * 매칭을 위한 채팅방 생성
     */
    @Transactional
    public Long createChatRoomForMatch(String matchId, Long member1Id, Long member2Id) {
        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.createRoom(matchId);
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

        log.info("채팅방 생성 완료. matchId={}, member1Id={}, member2Id={}", matchId, member1Id, member2Id);
        return chatRoom.getId();

    }

    /**
     * 매칭 ID로 채팅방 조회
     */
    @Transactional(readOnly = true)
    public ChatRoom findChatRoomByMatchId(String matchId) {
        return chatRoomRepository.findByMatchId(matchId).orElse(null);
    }

    /**
     * 채팅방 종료
     */
    @Transactional
    public void terminateChat(String matchId) {
        ChatRoom chatRoom = chatRoomRepository.findByMatchId(matchId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION));

        if (!chatRoom.isTerminated()) {
            chatRoom.terminate();
            chatRoomRepository.save(chatRoom);
            log.info("채팅방 종료 완료. matchId={}", matchId);
        }
    }

    /**
     * 멤버의 활성 채팅방 ID 조회
     */
    @Transactional(readOnly = true)
    public Long findActiveChatRoomIdForMember(Long memberId) {
        return chatRoomMemberRepository.findFirstByMember_MemberIdAndIsLeftFalse(memberId)
                .map(ChatRoomMember::getChatRoom)
                .filter(chatRoom -> !chatRoom.isTerminated())
                .map(ChatRoom::getId)
                .orElse(null);
    }

    /**
     * 채팅방 생성 또는 기존 채팅방 조회
     */
    @Transactional
    public ChatRoomResponse createOrGetChatRoom(Long myId, Long opponentId) {
        Optional<ChatRoom> existing = chatRoomRepository.findRoomByMembers(myId, opponentId);
        if (existing.isPresent()) {
            ChatRoom room = existing.get();

            ChatRoomMember currentMember = chatRoomMemberRepository
                    .findByChatRoomIdAndMember_MemberId(room.getId(), myId)
                    .orElseThrow(() -> new IllegalArgumentException("채팅방 멤버 정보를 찾을 수 없습니다"));

            ChatRoomMember opponent = chatRoomMemberRepository
                    .findByChatRoomIdAndMember_MemberIdNotWithMember(room.getId(), myId)
                    .orElse(null);

            return ChatRoomResponse.of(room, currentMember, opponent);
        }

        ChatRoom room = ChatRoom.createChatRoom(myId, opponentId);
        chatRoomRepository.save(room);

        Member me = memberRepository.findById(myId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원: " + myId));
        Member opponent = memberRepository.findById(opponentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원: " + opponentId));

        ChatRoomMember myMembership = chatRoomMemberRepository.save(ChatRoomMember.createChatRoomMember(room, me));
        ChatRoomMember opponentMembership = chatRoomMemberRepository.save(ChatRoomMember.createChatRoomMember(room, opponent));

        log.info("새 채팅방 생성 완료. myId={}, opponentId={}", myId, opponentId);

        // ✅ 3개 파라미터 메서드 사용
        return ChatRoomResponse.of(room, myMembership, opponentMembership);
    }
}
