package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.ScrimMyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.ScrimMyPageOnly;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScrimMyPageRepository extends CrudRepository<ScrimMyPage, UUID> {
    Optional<ScrimMyPageOnly> findByMyPageUUID(UUID myPageUUID);
    List<ScrimMyPageOnly> findAllBy();
    List<ScrimMyPageOnly> findByAcceptorIdOrRequestorId(Long acceptorId, Long requestorId);
    List<ScrimMyPageOnly> findByAccecptorId(Long acceptorId);
    List<ScrimMyPageOnly> findByRequestorId(Long requestorId);
}
