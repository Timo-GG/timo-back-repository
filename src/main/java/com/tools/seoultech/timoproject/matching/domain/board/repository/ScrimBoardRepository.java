package com.tools.seoultech.timoproject.matching.domain.board.repository;

import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.ScrimBoard;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScrimBoardRepository extends CrudRepository<ScrimBoard, UUID> {
    Optional<ScrimBoardOnly> findByBoardUUID(UUID boardId);
    List<ScrimBoardOnly> findAllBy();
    Optional<ScrimBoard> findByMemberId(Long memberId);
    boolean existsByMemberId(Long memberId);

}
