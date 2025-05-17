package com.tools.seoultech.timoproject.matching.domain.board.repository;

import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.DuoBoard;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.ranking.RankingInfoRedisRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DuoBoardRepository extends CrudRepository<DuoBoard, UUID> {
    Optional<DuoBoardOnly> findByBoardUUID(UUID id);
    List<DuoBoardOnly> findAllBy();
}
