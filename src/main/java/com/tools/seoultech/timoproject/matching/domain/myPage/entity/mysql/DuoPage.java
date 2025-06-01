package com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import com.tools.seoultech.timoproject.matching.service.converter.CertifiedMemberInfoConverter;
import com.tools.seoultech.timoproject.matching.service.converter.UserInfoConverter;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@DiscriminatorValue("DUO")
@Builder(toBuilder = true, builderMethodName = "updateBuilder")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DuoPage extends MyPage {
    @Enumerated(EnumType.STRING)
    private DuoMapCode mapCode;

    @Convert(converter = UserInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private UserInfo acceptorUserInfo;

    @Convert(converter = UserInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private UserInfo requestorUserInfo;

    @Convert(converter = CertifiedMemberInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private CertifiedMemberInfo acceptorMemberInfo;

    @Convert(converter = CertifiedMemberInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private CertifiedMemberInfo requestorMemberInfo;

    @Builder(builderMethodName = "createBuilder")
    public DuoPage(MatchingStatus matchingStatus,
                      Member acceptor, Member requestor, DuoMapCode mapCode,
                      UserInfo acceptorUserInfo, UserInfo requestorUserInfo,
                      CertifiedMemberInfo acceptorMemberInfo, CertifiedMemberInfo requestorMemberInfo){

        super(MatchingCategory.DUO, matchingStatus, acceptor, requestor);
        this.mapCode = mapCode;
        this.acceptorUserInfo = acceptorUserInfo;
        this.requestorUserInfo = requestorUserInfo;
        this.acceptorMemberInfo = acceptorMemberInfo;
        this.requestorMemberInfo = requestorMemberInfo;
    }
}
