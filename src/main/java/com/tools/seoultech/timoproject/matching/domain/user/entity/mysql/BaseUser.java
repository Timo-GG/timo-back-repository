package com.tools.seoultech.timoproject.matching.domain.user.entity.mysql;

import com.tools.seoultech.timoproject.matching.domain.board.entity.mysql.BaseBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private MemberAccount memberAccount;

    protected BaseUser(MemberAccount memberAccount) {this.memberAccount = memberAccount;}
}
