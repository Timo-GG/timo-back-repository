package com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import com.tools.seoultech.timoproject.matching.service.converter.CertifiedMemberInfoConverter;
import com.tools.seoultech.timoproject.matching.service.converter.ListCompactMemberInfoConverter;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@DiscriminatorValue("SCRIM")
@Builder(toBuilder = true, builderMethodName = "updateBuilder")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrimPage extends MyPage {
    private Integer headCount;

    @Enumerated(EnumType.STRING)
    private ScrimMapCode mapCode;

    @Convert(converter = ListCompactMemberInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private List<CompactMemberInfo> acceptorPartyInfo;

    @Convert(converter = ListCompactMemberInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private List<CompactMemberInfo> requestorPartyInfo;

    @Convert(converter = CertifiedMemberInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private CertifiedMemberInfo acceptorMemberInfo;

    @Convert(converter = CertifiedMemberInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private CertifiedMemberInfo requestorMemberInfo;

    @Builder(builderMethodName = "createBuilder")
    public ScrimPage(MatchingStatus matchingStatus,
                     Member acceptor, Member requestor, Integer headCount, ScrimMapCode mapCode,
                     List<CompactMemberInfo> acceptorPartyInfo, List<CompactMemberInfo> requestorPartyInfo,
                     CertifiedMemberInfo acceptorMemberInfo, CertifiedMemberInfo requestorMemberInfo){

        super(MatchingCategory.SCRIM, matchingStatus, acceptor, requestor);
        this.headCount = headCount;
        this.mapCode = mapCode;
        this.acceptorPartyInfo = acceptorPartyInfo;
        this.requestorPartyInfo = requestorPartyInfo;
        this.acceptorMemberInfo = acceptorMemberInfo;
        this.requestorMemberInfo = requestorMemberInfo;
    }
}
