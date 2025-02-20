package com.tools.seoultech.timoproject.chat.repository;

import com.tools.seoultech.timoproject.chat.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByRoom(String room);

    Slice<Message> findByChatRoomIdOrderByIdDesc(Long chatRoomId, Pageable pageable);

    @Query("select m "
            + "from Message m "
            + "where m.chatRoom.id = :roomId "
            + "and m.id < :messageId "
            + "order by m.id desc")
    Slice<Message> findByRoomIdLessThanIdOrderByIdDesc(long roomId, long messageId, Pageable pageable);
}
