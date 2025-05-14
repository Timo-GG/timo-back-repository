package com.tools.seoultech.timoproject.matching.domain.myPage.entity.repository;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.ScrimMyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScrimMyPageRepository extends JpaRepository<ScrimMyPage, UUID> {
}
