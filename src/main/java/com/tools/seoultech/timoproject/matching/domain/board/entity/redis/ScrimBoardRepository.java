package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScrimBoardRepository extends JpaRepository<ScrimBoard, UUID> {
}
