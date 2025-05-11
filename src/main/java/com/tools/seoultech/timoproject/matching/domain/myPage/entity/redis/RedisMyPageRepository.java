package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.redis.om.spring.repository.RedisDocumentRepository;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RedisMyPageRepository extends RedisDocumentRepository<RedisMyPage, UUID> {
    List<RedisMyPage> findAllByMatchingCategory(MatchingCategory matchingCategory);

    List<RedisMyPage> findAllByRequestorMemberId(Long requestorMemberId);

    List<RedisMyPage> findAllByAcceptorMemberId(Long acceptorMemberId);
}
