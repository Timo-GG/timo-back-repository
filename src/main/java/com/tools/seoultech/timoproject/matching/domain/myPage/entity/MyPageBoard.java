package com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.mysql.BaseUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageBoard extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name="myPage_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "requestor_id", nullable = false, updatable = false)
    private BaseUser requestor;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "acceptor_id", nullable = false, updatable = false)
    private BaseUser acceptor;
}
