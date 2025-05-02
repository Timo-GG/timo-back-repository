package com.tools.seoultech.timoproject.version2.matching.domain.board.entity.mysql;


import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.mysql.BaseUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseSearchBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private BaseUser baseUser;

    private String memo;

    protected BaseSearchBoard(BaseUser baseUser, String memo) {
        this.baseUser = baseUser;
        this.memo = memo;
    }
}
