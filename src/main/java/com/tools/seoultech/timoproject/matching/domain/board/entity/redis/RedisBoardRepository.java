package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RedisBoardRepository extends CrudRepository<RedisBoard, UUID> {

    List<RedisBoard> findAllByMatchingCategory(MatchingCategory matchingCategory);
}
