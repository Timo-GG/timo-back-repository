package com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType;

import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartyMemberInfo {
    private String puuid;
    private String gameName;
    private String tagLine;
    private PlayPosition myPosition;
}