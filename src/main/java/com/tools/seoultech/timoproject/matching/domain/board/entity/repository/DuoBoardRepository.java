package com.tools.seoultech.timoproject.matching.domain.board.entity.repository;

import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.DuoBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DuoBoardRepository extends JpaRepository<DuoBoard, UUID> {
}
