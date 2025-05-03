package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@RedisHash(value = "SearchBoard", timeToLive = 15*60)
@Getter
@JsonIgnoreProperties(value = {"matchingCategory"}, allowGetters = true, allowSetters = false)
public class RedisBoardDTO<T extends BoardDTO.Response> {
    @Id
    private final String uuid;
    private final String userUUID;

    private final T body;
    private final String memo;

    @Indexed
    // 서버 내부 필드. 역직렬화에 사용되지 않음. 직렬화에서는 편의성 때문에 보여주긴 함.
    private final MatchingCategory matchingCategory;

    @Builder
    @PersistenceCreator
    public RedisBoardDTO(T body, String userUUID, String memo) {
        this.uuid = UUID.randomUUID().toString();
        this.matchingCategory = body.getMatchingCategory();
        this.userUUID = userUUID;
        this.body = body;
        this.memo = memo;
    }
}
