package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;

import com.redis.om.spring.repository.RedisDocumentRepository;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisMyPage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface RedisBoardRepository extends RedisDocumentRepository<RedisBoard, String> {
    List<RedisBoard> findAllByMatchingCategory(MatchingCategory matchingCategory);
}
