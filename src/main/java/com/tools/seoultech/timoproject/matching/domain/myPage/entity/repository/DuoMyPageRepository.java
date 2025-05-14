package com.tools.seoultech.timoproject.matching.domain.myPage.entity.repository;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.DuoMyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DuoMyPageRepository extends JpaRepository<DuoMyPage, UUID> {
}
