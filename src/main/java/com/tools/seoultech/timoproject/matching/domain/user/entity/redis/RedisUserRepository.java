package com.tools.seoultech.timoproject.matching.domain.user.entity.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisUserRepository extends CrudRepository<RedisUserDTO<?>, String> {
}
