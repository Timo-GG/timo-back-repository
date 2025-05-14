package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DuoMyPageRepository extends JpaRepository<DuoMyPage, UUID> {
}
