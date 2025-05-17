package com.tools.seoultech.timoproject.matching.domain.myPage.repository;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.DuoMyPage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DuoMyPageRepository extends CrudRepository<DuoMyPage, UUID> {
}
