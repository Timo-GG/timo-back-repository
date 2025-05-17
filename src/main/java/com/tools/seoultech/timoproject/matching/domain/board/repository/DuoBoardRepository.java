package com.tools.seoultech.timoproject.matching.domain.board.repository;

import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.DuoBoard;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DuoBoardRepository extends JpaRepository<DuoBoard, UUID> {
    Optional<DuoBoardOnly> findByBoardUUID(UUID id);
    List<DuoBoardOnly> findAllBy();
}
