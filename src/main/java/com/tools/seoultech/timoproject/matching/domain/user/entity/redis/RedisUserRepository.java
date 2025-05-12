package com.tools.seoultech.timoproject.matching.domain.user.entity.redis;

import com.redis.om.spring.repository.RedisDocumentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface RedisUserRepository extends RedisDocumentRepository<RedisUser, String> {
    List<RedisUser> findAllByMemberAccount_MemberId(Long memberId);
}
