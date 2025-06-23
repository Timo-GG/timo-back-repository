package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisScrimPageOnly;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RedisScrimPageRepository extends CrudRepository<RedisScrimPage, UUID> {
    Optional<RedisScrimPageOnly> findByMyPageUUID(UUID myPageUUID);
    List<RedisScrimPageOnly> findAllBy();
    List<RedisScrimPageOnly> findByAcceptorIdOrRequestorId(Long acceptorId, Long requestorId);
    List<RedisScrimPageOnly> findByAcceptorId(Long acceptorId);
    List<RedisScrimPageOnly> findByRequestorId(Long requestorId);
    Optional<RedisScrimPageOnly> findByRequestorIdAndBoardUUID(Long requestorId, UUID boardUUID);
}
