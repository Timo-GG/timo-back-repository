package com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.service.converter.CompactMemberInfoConverter;
import com.tools.seoultech.timoproject.matching.service.converter.UserInfoConverter;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("DUO")
public class DuoPage extends MyPage {
    @Convert(converter = UserInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private UserInfo acceptorUserInfo;

    @Convert(converter = UserInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private UserInfo requestorUserInfo;

    @Convert(converter = CompactMemberInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private CompactMemberInfo acceptorMemberInfo;

    @Convert(converter = CompactMemberInfoConverter.class)
    @Column(columnDefinition = "JSON")
    private CompactMemberInfo requestorMemberInfo;
}
