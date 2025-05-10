package com.tools.seoultech.timoproject.matching.domain.user.entity.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RedisUserRepository extends CrudRepository<RedisUser, UUID> {

}
