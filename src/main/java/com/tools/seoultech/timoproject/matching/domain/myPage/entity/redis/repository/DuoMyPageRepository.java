package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.DuoMyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.DuoMyPageOnly;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DuoMyPageRepository extends CrudRepository<DuoMyPage, UUID> {
    Optional<DuoMyPageOnly> findByMyPageUUID(UUID myPageUUID);
    List<DuoMyPageOnly> findAllBy();
    List<DuoMyPageOnly> findByAcceptorIdOrRequestorId(Long acceptorId, Long requestorId);
    List<DuoMyPageOnly> findByAccecptorId(Long acceptorId);
    List<DuoMyPageOnly> findByRequestorId(Long requestorId);
}
