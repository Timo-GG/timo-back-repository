package com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import com.tools.seoultech.timoproject.matching.service.converter.ListCompactMemberInfoConverter;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@DiscriminatorValue("SCRIM")
public class ScrimPage extends MyPage {
    @Convert(converter = ListCompactMemberInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private List<CompactMemberInfo> acceptorPartyInfo;

    @Convert(converter = ListCompactMemberInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private List<CompactMemberInfo> requestorPartyInfo;
}
