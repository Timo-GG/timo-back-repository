package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisDuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisDuoPageOnly;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RedisDuoPageRepository extends CrudRepository<RedisDuoPage, UUID> {
    Optional<RedisDuoPageOnly> findByMyPageUUID(UUID myPageUUID);
    List<RedisDuoPageOnly> findAllBy();
    List<RedisDuoPageOnly> findByAcceptorIdOrRequestorId(Long acceptorId, Long requestorId);
    List<RedisDuoPageOnly> findAllByAcceptorId(Long acceptorId);
    List<RedisDuoPageOnly> findAllByRequestorId(Long requestorId);
}
