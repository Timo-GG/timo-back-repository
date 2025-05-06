package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RedisMyPageRepository extends CrudRepository<UUID, RedisMyPage> {
}
