package com.tools.seoultech.timoproject.chat.service;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.domain.Message;
import com.tools.seoultech.timoproject.chat.dto.ChatMessageDTO;
import com.tools.seoultech.timoproject.chat.dto.ChatRoomMetadata;
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
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService implements DisposableBean {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberRepository memberRepository;

    private static final int MAX_QUEUE_SIZE = 300;   // 큐 최대 크기 (예: 100)
    private static final int FLUSH_SIZE = 100;        // 한 번에 DB에 기록할 개수 (예: 30)

    // 메시지 본문을 저장할 캐시: roomId -> Queue<Message>
    private static final Map<Long, Queue<Message>> messageMap = new ConcurrentHashMap<>();

    // 채팅방 메타데이터를 저장할 캐시: roomId -> ChatRoomMetadata
    private static final Map<Long, ChatRoomMetadata> chatRoomMetadataMap = new ConcurrentHashMap<>();


    // ====================================
    // (기존) 메시지 즉시 저장 로직
    // ====================================
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

        log.info("메시지 저장(DB 즉시): content - {}, senderId - {}", message.getContent(), message.getSenderId());

        // 4) DTO 변환 후 반환
        return ChatMessageDTO.builder()
                .messageId(message.getId())
                .roomId(chatRoom.getId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .build();
    }

    // ====================================
    // 1) 메시지 + 메타데이터 캐싱 로직
    // ====================================
    @Transactional
    public void saveMessageWithCache(ChatMessageDTO dto) {
        // 1) 채팅방 확인
        ChatRoom chatRoom = chatRoomRepository.findById(dto.roomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        // 2) 메시지 캐싱
        Message message = Message.createMessage(chatRoom, dto.senderId(), dto.content());
        messageMap.putIfAbsent(dto.roomId(), new LinkedList<>());
        Queue<Message> queue = messageMap.get(dto.roomId());
        queue.add(message);

        if (queue.size() >= MAX_QUEUE_SIZE) {
            flushMessageQueue(dto.roomId());  // ✅ 기존 `FLUSH_SIZE` 개수만큼이 아니라 전체 배치 저장
        }

        // ✅ 메타데이터 업데이트 최소화 (DB 접근 최소화)
        chatRoomMetadataMap.putIfAbsent(dto.roomId(), new ChatRoomMetadata(dto.roomId(), dto.content(), LocalDateTime.now(), dto.senderId(), new HashMap<>()));
        ChatRoomMetadata meta = chatRoomMetadataMap.get(dto.roomId());

        for (ChatRoomMember crm : chatRoomMemberRepository.findByChatRoom(chatRoom)) {
            if (!crm.getMember().getId().equals(dto.senderId())) {
                meta.incrementUnread(crm.getMember().getId());
            }
        }
    }

    /**
     * roomId에 해당하는 메시지 큐에서 flushSize개를 DB에 기록
     */
    private void flushMessageQueue(Long roomId) {
        Queue<Message> queue = messageMap.get(roomId);
        if (queue == null || queue.isEmpty()) return;

        List<Message> batchList = new ArrayList<>(queue);
        messageRepository.saveAll(batchList);
        queue.clear(); // ✅ DB에 저장한 후 캐시 비우기

    }

    /**
     * flushQueue에 있는 메시지를 DB에 일괄 저장
     */
    private void commitMessageQueue(Queue<Message> flushQueue) {
        int size = flushQueue.size();
        if (size == 0) return;

        List<Message> batchList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Message msg = flushQueue.poll();
            if (msg != null) {
                batchList.add(msg);
            }
        }
        messageRepository.saveAll(batchList);
        log.info("[commitMessageQueue] {}건의 메시지를 DB에 배치 저장 완료", size);
    }

    // ====================================
    // 2) 메타데이터 배치 플러시
    // ====================================
    @Scheduled(fixedDelay = 30000) // 예: 30초마다
    @Transactional
    public void scheduledFlush() {
        for (Long roomId : messageMap.keySet()) {
            if (messageMap.get(roomId).size() >= FLUSH_SIZE) { // ✅ 충분히 쌓인 경우에만 Flush 실행
                flushMessageQueue(roomId);
            }
        }
    }

    /**
     * chatRoomMetadataMap에 누적된 메타데이터를 DB에 반영
     */
    private void flushChatRoomMetadata() {
        for (Map.Entry<Long, ChatRoomMetadata> entry : chatRoomMetadataMap.entrySet()) {
            ChatRoomMetadata meta = entry.getValue();
            Long roomId = meta.getRoomId();

            // (1) ChatRoom의 lastMessage 갱신
            chatRoomRepository.findById(roomId).ifPresent(chatRoom -> {
                chatRoom.updateLastMessage(
                        // pseudoMessage를 굳이 만들 필요 없이 setter 가능
                        Message.createMessage(chatRoom, meta.getLastMessageSenderId(), meta.getLastMessage())
                );
                chatRoomRepository.save(chatRoom);
            });

            // (2) unreadCount 배치 업데이트
            List<ChatRoomMember> updateList = new ArrayList<>();
            for (Map.Entry<Long, Integer> e : meta.getUnreadMap().entrySet()) {
                chatRoomMemberRepository.findByChatRoomIdAndMemberId(roomId, e.getKey())
                        .ifPresent(crm -> {
                            crm.increaseUnreadCount();
                            updateList.add(crm);
                        });
            }
            chatRoomMemberRepository.saveAll(updateList);  // ✅ saveAll() 사용
        }
        // 모든 메타데이터 flush 후, 캐시 비우기 (혹은 재설정)
        chatRoomMetadataMap.clear();
        log.info("[flushChatRoomMetadata] 메타데이터 배치 저장 완료");
    }

    // ====================================
    // 3) 종료 시 처리
    // ====================================
    @Override
    public void destroy() throws Exception {
        // 3-1) 남은 메시지 모두 flush
        for (Long roomId : messageMap.keySet()) {
            flushMessageQueue(roomId);
        }
        // 3-2) 메타데이터 flush
        flushChatRoomMetadata();
    }

    // ====================================
    // 5) 채팅방 메타데이터(마지막 메시지, unreadCount 등) 즉시 업데이트 예시
    // ====================================
    private void updateChatRoomMetaData(ChatRoom chatRoom, ChatMessageDTO dto) {
        // unreadCount 업데이트
        List<ChatRoomMember> members = chatRoomMemberRepository.findByChatRoom(chatRoom);
        for (ChatRoomMember member : members) {
            if (!member.getMember().getId().equals(dto.senderId())) {
                member.increaseUnreadCount();
            }
        }
        chatRoomMemberRepository.saveAll(members);

        // lastMessage 갱신
        Message pseudoMessage = Message.createMessage(chatRoom, dto.senderId(), dto.content());
        chatRoom.updateLastMessage(pseudoMessage);
        chatRoomRepository.save(chatRoom);
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

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getMessagesWithCache(Long roomId, int page, int size) {
        // 캐시에 저장된 메시지 조회
        Queue<Message> queue = messageMap.get(roomId);
        if (queue == null || queue.isEmpty()) {
            return Collections.emptyList();
        }

        // 페이징 처리
        List<Message> messageList = new ArrayList<>(queue);
        int totalMessages = messageList.size();
        int start = Math.max(0, page * size); // ✅ 음수 방지

        // ✅ start가 전체 메시지 개수를 초과하면 빈 리스트 반환
        if (start >= totalMessages) {
            return Collections.emptyList();
        }

        int end = Math.min(start + size, totalMessages); // ✅ end가 totalMessages를 초과하지 않도록 제한
        List<Message> subList = messageList.subList(start, end);

        // Message -> ChatMessageDTO 변환
        return subList.stream()
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

        log.info("✅ 채팅방 생성 및 멤버 가입 완료. matchId={}", matchId);
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
                .orElse(null);
    }
}
