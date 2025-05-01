package com.tools.seoultech.timoproject.version2.matching.domain.board.entity.redis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tools.seoultech.timoproject.version2.matching.domain.board.dto.SearchBoardDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.MatchingCategory;
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
public class RedisSearchBoardDTO<T extends SearchBoardDTO.BoardResponseDTOInterface> {
    @Id
    private final String uuid;

    @Indexed
    // 서버 내부 필드. 역직렬화에 사용되지 않음. 직렬화에서는 편의성 때문에 보여주긴 함.
    private final MatchingCategory matchingCategory;

    private final T body;
    private final String memo;

    @Builder
    @PersistenceCreator
    protected RedisSearchBoardDTO(String user_uuid, T body, String memo) {
        this.uuid = UUID.randomUUID().toString();
        this.matchingCategory = body.getMatchingCategory();
        this.body = body;
        this.memo = memo;
    }
}
