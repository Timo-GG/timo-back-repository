package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RedisMyPageRepository extends CrudRepository<RedisMyPage, UUID> {
    List<RedisMyPage> findAllByMatchingCategory(MatchingCategory matchingCategory);

    List<RedisMyPage> findByRequestor(RedisUser requestor);

    List<RedisMyPage> findByAcceptor(RedisUser acceptor);
}
