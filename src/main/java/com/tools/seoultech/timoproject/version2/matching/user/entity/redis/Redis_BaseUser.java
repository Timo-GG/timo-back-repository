package com.tools.seoultech.timoproject.version2.matching.user.entity.redis;

import com.tools.seoultech.timoproject.global.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("baseUser")
@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class Redis_BaseUser extends BaseEntity {
    private Long memberId;

    protected Redis_BaseUser(Long memberId ){
        this.memberId = memberId;
    }
}
