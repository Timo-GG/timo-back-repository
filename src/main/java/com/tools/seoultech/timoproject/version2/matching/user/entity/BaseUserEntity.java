package com.tools.seoultech.timoproject.version2.matching.user.entity;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BaseUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="member_id")
    private MemberAccount memberAccount;

    protected BaseUserEntity(MemberAccount memberAccount) {
        this.memberAccount = memberAccount;
    }
}
