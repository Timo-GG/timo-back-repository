package com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.matching.domain.board.entity.mysql.BaseBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import com.tools.seoultech.timoproject.matching.domain.user.entity.mysql.BaseUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyPage extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name="mypage_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    MatchingCategory matchingCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    MatchingStatus status;
}
