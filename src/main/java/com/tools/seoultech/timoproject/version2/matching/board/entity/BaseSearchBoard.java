package com.tools.seoultech.timoproject.version2.matching.board.entity;


import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.version2.matching.user.entity.BaseUserEntity;
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
public class BaseSearchBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private BaseUserEntity baseUser;

    private String memo;

    protected BaseSearchBoard(BaseUserEntity baseUser, String memo) {
        this.baseUser = baseUser;
        this.memo = memo;
    }
}
