package com.tools.seoultech.timoproject.version2.matching.user.entity;

import com.tools.seoultech.timoproject.global.BaseEntity;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("baseUser")
//@AllArgsConstructor
@NoArgsConstructor
public class Redis_BaseUserEntity extends BaseEntity {
    private Long memberId;

    protected Redis_BaseUserEntity(Long memberId ){
        this.memberId = memberId;
    }
}
