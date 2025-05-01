package com.tools.seoultech.timoproject.version2.matching.domain.user.entity.redis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.version2.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.RiotAccount;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@RedisHash(value = "userDTO", timeToLive = 15 * 60)
@Getter
@JsonIgnoreProperties(value = {"matchingCategory"}, allowGetters = true, allowSetters = false)
public class RedisUserDTO<T extends UserDTO.UserResponseDTOInterface> {
    @Id
    private final String uuid;

    private final Long memberId;
    private final RiotAccount riotAccount;

    @Indexed
    // 서버 내부 필드. 역직렬화에 사용되지 않음. 직렬화에서는 편의성 때문에 보여주긴 함.
    private final MatchingCategory matchingCategory;

    private final T body;

    @Builder
    @PersistenceCreator
    public RedisUserDTO(Long memberId, RiotAccount riotAccount, T body) {
        this.uuid = UUID.randomUUID().toString();
        this.memberId = memberId;
        this.riotAccount = riotAccount;
        this.matchingCategory = body.getMatchingCategory();
        this.body = body;
    }
}
