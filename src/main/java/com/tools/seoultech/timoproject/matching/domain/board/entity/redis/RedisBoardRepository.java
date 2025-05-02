package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisBoardRepository extends CrudRepository<RedisBoardDTO<?>, String> {
}
